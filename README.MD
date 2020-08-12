# Code golf dataset
To find all java files: `find -type f -name "*.java" > java_files.txt`
To select random lines from the generated file and view selected files: `less $(python3 select_random_lines.py java_files.txt 5 --seed 17)`
Now you can change the seed and choose files.
To save file names for the chosen seed: `python3 select_random_lines.py java_files.txt 5 --dest code_golf_small.json --seed 17`
There is also a `python3 select_random_lines.py --help`
