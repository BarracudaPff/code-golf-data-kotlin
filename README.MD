# Code golf dataset

##### Find all java files
```shell script
find . -type f -name "*.java" > java_files.txt
```
Newline separated file paths will be in `java_files.txt`

##### Select random files
The following line reads *N* random lines from the generated file and passes selected files to `less`.
Navigation between files in `less` is `:n` for *next* and `:p` for *previous*.
```shell script
less $(python3 select_random_lines.py java_files.txt 5 --seed 17)
```
You can change the sample by varying the seed. 

##### Save file names
Execute the following with your chosen `seed`: 
```shell script
python3 select_random_lines.py java_files.txt 5 --dest code_golf_small.json --seed 17 --json
``` 
If `--json` option is omitted, this script will save newline separated file paths to the specified file.
If `--dest` is not specified, the result will be printed to `stdout`.

##### Help
There is also a `python3 select_random_lines.py --help`
