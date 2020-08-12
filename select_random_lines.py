import argparse
import json
import sys
import random


def select_random_lines(fname, num_lines, seed):
    random.seed(seed)
    with open(fname, "r", encoding="utf-8") as file:
        lines = file.readlines()
        # crop newline
        return [line[:-1] for line in random.sample(lines, k=num_lines)]


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
