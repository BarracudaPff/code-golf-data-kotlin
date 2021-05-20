from argparse import ArgumentParser
from typing import List, Tuple
from shutil import copyfile

import random
import json
import glob
import os


def select_files(files_dir: str, max_sz: float, seed: int) -> Tuple[List[str], int]:
    random.seed(seed)

    files = list(glob.iglob(os.path.join(files_dir, '**', '*.[kK][tT]'), recursive=True))
    random.shuffle(files)

    selected_files = []
    curr_size = 0
    for file in files:
        if curr_size >= max_sz:
            break

        if "test" in file or "foo" in file.lower():
            continue

        curr_size += os.path.getsize(file)
        selected_files.append(file)

    return selected_files, curr_size


def copy_files_to_dir(selected_files_dir: str, selected_files: List[str]) -> List[str]:
    file_paths_new = []
    for file in selected_files:
        file_out = os.path.join(selected_files_dir, '/'.join(file.split('/')[1:]))
        folder = os.path.dirname(file_out)
        os.makedirs(folder, exist_ok=True)

        copyfile(file, file_out)
        file_paths_new.append(file_out)

    return file_paths_new


if __name__ == '__main__':
    args = ArgumentParser()

    args.add_argument("--files_dir", type=str, required=True, help="Directory with files.")
    args.add_argument("--selected_files_dir", type=str, required=True, help="Directory with selected files.")
    args.add_argument("--output_path", type=str, required=True, help="Json path.")
    args.add_argument("--sz", type=float, required=True, help="Max size of files in MB.")
    args.add_argument("--seed", type=int, default=17, help="Random seed")
    args = args.parse_args()

    selected_files, sz = select_files(args.files_dir, args.sz * 1024 ** 2, args.seed)
    print(f"Written size: {sz / 1024 ** 2:.3f}")

    selected_files = copy_files_to_dir(args.selected_files_dir, selected_files)
    selected_files = ["./" + path for path in selected_files]

    res = json.dumps({"evaluationRoots": selected_files}, indent=4)
    with open(args.output_path, "w") as f:
        f.write(res + "\n")
