/*
 * The MIT License
 * Copyright © 2014-2019 Ilkka Seppälä
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package concreteextensions;

import abstractextensions.SergeantExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import units.SergeantUnit;

/**
 * Class defining Sergeant.
 */
public class Sergeant implements SergeantExtension {

  private static final Logger LOGGER = LoggerFactory.getLogger(Sergeant.class);

  private final SergeantUnit unit;

  public Sergeant(SergeantUnit sergeantUnit) {
    this.unit = sergeantUnit;
  }

  @Override
  public void sergeantReady() {
    LOGGER.info("[Sergeant] " + unit.getName() + " is ready! ");
  }
}