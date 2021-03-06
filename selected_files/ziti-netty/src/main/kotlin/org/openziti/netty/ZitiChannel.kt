package org.openziti.netty
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.*
import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler
class ZitiChannel(parent: ServerChannel, val peer: AsynchronousSocketChannel):
    AbstractChannel(parent),
    Logged by ZitiLog() {
    override fun doBeginRead() {
        d("starting read")
        val buf = ByteBuffer.allocate(1024)
        peer.read(buf, this, object : CompletionHandler<Int, ZitiChannel>{
            override fun completed(len: Int, c: ZitiChannel) {
                t("received $len bytes")
                if (len > 0) {
                    buf.flip()
                    val b = Unpooled.copiedBuffer(buf)
                    buf.clear()
                    c.pipeline().fireChannelRead(b)
                } else if (len == -1) {
                    eventLoop().execute {
                        unsafe().close(voidPromise())
                    }
                    return
                }
                peer.read(buf, c, this)
            }
            override fun failed(exc: Throwable, c: ZitiChannel) {
                c.pipeline().fireExceptionCaught(exc)
            }
        })
    }
    override fun isActive(): Boolean {
        return peer.isOpen
    }
    override fun isCompatible(loop: EventLoop?): Boolean {
        return true
    }
    override fun remoteAddress0(): SocketAddress = peer.remoteAddress
    override fun metadata(): ChannelMetadata = META
    override fun doBind(localAddress: SocketAddress?) {
        error("unsupported operation")
    }
    override fun doClose() {
        peer.close()
    }
    override fun newUnsafe(): AbstractUnsafe = AsyncUnsafe()
    override fun config(): ChannelConfig {
        return DefaultChannelConfig(this)
    }
    override fun localAddress0(): SocketAddress = peer.localAddress
    override fun isOpen(): Boolean = peer.isOpen
    override fun doWrite(buf: ChannelOutboundBuffer) {
        val spinCount = config().writeSpinCount
        for(i in 0 until spinCount) {
            val msg: Any? = buf.current()
            when (msg) {
                is ByteBuf -> {
                    if (!msg.isReadable)
                        buf.remove()
                    else {
                        val b = ByteBuffer.allocate(msg.readableBytes())
                        msg.readBytes(b)
                        b.flip()
                        val flushed = peer.write(b).get()
                        buf.progress(flushed.toLong())
                        if (!msg.isReadable)
                            buf.remove()
                    }
                }
                null -> return
            }
        }
        if (buf.current() != null) {
            t("need to flush again")
            eventLoop().execute {
                unsafe().flush()
            }
        }
    }
    override fun doDisconnect() {
        peer.close()
    }
    private inner class AsyncUnsafe: AbstractChannel.AbstractUnsafe() {
        override fun connect(remoteAddress: SocketAddress?, localAddress: SocketAddress?, promise: ChannelPromise) {
            promise.setFailure(UnsupportedOperationException())
        }
    }
    companion object {
        val META = ChannelMetadata(false)
    }
}