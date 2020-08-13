import argparse
import json
import sys
import random
import os
from collections import defaultdict


def select_random_lines(fname, num_lines, seed):
    random.seed(seed)
    with open(fname, "r", encoding="utf-8") as file:
        lines = file.readlines()
        if len(lines) < num_lines:
            raise ValueError(f"{fname} doesn't have {num_lines} lines")

        # crop newline
        res_lines = []
        repo_lists = defaultdict(list)

        for line in lines:
            repo_name = line.split(os.sep)[1]
            repo_lists[repo_name].append(line[:-1])

        def take_lines(cur_repo_files):
            files_left_in_repo = len(cur_repo_files)
            files_to_take = min(files_left_in_repo, lines_left / repos_left)
            files_left_to_take = files_to_take
            test_count = 0

            def line_filter(cur_line):
                nonlocal test_count
                if "test" in cur_line:
                    if test_count >= files_to_take/2:
                        return False
                    else:
                        test_count += 1
                if "android" in cur_line:
                    return False
                if line in res_lines:
                    return False
                return True

            random.shuffle(cur_repo_files)
            repo_iter = iter(cur_repo_files)
            cur_repo_lines = []

            while files_left_to_take > 0 and files_left_in_repo > 0:
                cur_line = next(repo_iter)
                files_left_in_repo -= 1
                while not line_filter(cur_line):
                    cur_line = next(repo_iter)
                    files_left_in_repo -= 1
                cur_repo_lines.append(cur_line)
                files_left_to_take -= 1
            return cur_repo_lines

        lines_left = num_lines

        while lines_left > 0:
            repos_left = len(repo_lists)
            for _, repo_files in repo_lists.items():
                repo_lines = take_lines(repo_files)
                res_lines.extend(repo_lines)
                lines_left -= len(repo_lines)
                repos_left -= 1

        return res_lines


if __name__ == "__main__":
    argparser = argparse.ArgumentParser(description="Choose random lines from file")
    argparser.add_argument("fname", type=str, help="Source file")
    argparser.add_argument("num_lines", type=int, help="Number of lines to choose")
    argparser.add_argument("--dest", type=str, help="Destination file")
    argparser.add_argument("--seed", type=int, default=17, help="Random seed")
    argparser.add_argument("--json", action="store_true", help="Output in json")
    args = argparser.parse_args()

    selected_lines = select_random_lines(args.fname, args.num_lines, args.seed)
    if args.json:
        res = json.dumps({"evaluationRoots": selected_lines}, indent=4)
    else:
        res = "\n".join(selected_lines)

    if args.dest is not None:
        with open(args.dest, "w") as f:
            f.write(res + "\n")
    else:
        sys.stdout.write(res + "\n")
