#!/bin/bash
# run with ./src/testers/execute_jott.sh 

# Ensure Java files are compiled
javac -d bin -sourcepath src $(find src -name "*.java")

# Check if compilation succeeded
if [ $? -ne 0 ]; then
    echo "Compilation failed. Exiting."
    exit 1
fi

# Initialize counters for passed and failed tests
passed=0
failed=0

# Loop through all .jott files in the phase3testcases directory
for jott_file in ./phase3testcases/*.jott; do
    # Extract expected output from the first line of the file
    expected=$(head -n 1 "$jott_file" | sed 's/^#//')

    # Run the Java program and capture its output
    actual=$(java -cp bin msc.Jott "$jott_file" 2>&1)

    # Normalize outputs by removing trailing spaces
    expected=$(echo -e "$expected" | sed '/^$/d')
    actual=$(echo -e "$actual" | sed '/^$/d')

    # Determine if this is a Semantic Error case
    if [[ "$expected" == "Semantic Error" ]]; then
        # Check if the actual output contains "Semantic Error"
        if [[ "$actual" == "Semantic Error"* ]]; then
            echo "PASS: $jott_file"
            echo "Output:   $actual"
            ((passed++))
        else
            echo "FAIL: $jott_file"
            echo "Expected: Semantic Error"
            echo "Actual:   $actual"
            ((failed++))
        fi
    else
        # Compare the expected and actual outputs for normal cases
        if [ "$expected" == "$actual" ]; then
            echo "PASS: $jott_file"
            ((passed++))
        else
            echo "FAIL: $jott_file"
            echo "Expected: $expected"
            echo "Actual:   $actual"
            ((failed++))
        fi
    fi
    echo "-------------------------------------------------"
done

# Display the final tally of results
echo "Testing complete."
echo "Passed: $passed"
echo "Failed: $failed"