import argparse
import json
import sys
import random
import os
from collections import defaultdict
from typing import Iterable, List, Tuple

from tqdm import tqdm


def extract(
    files: Iterable[str],
    output_dir: str,
    example_split_sym: str = "␢",
    meta_info_split_sym: str = "𐌼",
    filepath_split_sym: str = "₣",
) -> None:
    for file in tqdm(files):
        with open(file, "r", encoding="utf-8") as f:
            file_path = ""
            content = []
            is_content = False
            is_filepath = False
            for line in f:
                line = line.rstrip()
                if line == example_split_sym:
                    is_content = False
                    write_file(file_path, content, output_dir)
                    file_path = ""
                    content = []
                    continue
                if line == meta_info_split_sym:
                    is_filepath = True
                    continue
                if line == filepath_split_sym:
                    is_content = True
                    continue

                if is_filepath:
                    is_filepath = False
                    file_path = line

                if is_content:
                    content.append(line)


def write_file(file_path: str, content: List[str], output_dir) -> None:
    if file_path == "" or len(content) == 0:
        return

    file_path = os.path.join(output_dir, file_path)
    folder = os.path.dirname(file_path)
    os.makedirs(folder, exist_ok=True)

    content = "\n".join(content)
    with open(file_path, "w") as f:
        f.write(content)


if __name__ == "__main__":
    args = argparse.ArgumentParser()

    args.add_argument("--files", nargs="+", required=True, help="Source file")
    args.add_argument("--output_dir", type=str, required=True, help="Output directory")

    args = args.parse_args()
    extract(args.files, args.output_dir)
