#!/bin/bash

OUT_PATH="func_tests/out"

testPositive()
{
	NAME=$1
	DATA=$2
	DESC=$3
	if java -jar build/libs/app-cli.jar <<<"$DATA" >/dev/null; then
		mv generated.bmp "$OUT_PATH"/"$NAME".bmp
		echo "$DESC" >"$OUT_PATH"/"$NAME"_desc.txt
		echo -e "Test: $NAME -- SUCCESS\n"
	else
		FAILED=$((FAILED + 1))
		echo -e "Test: $NAME -- FAILED\n"
	fi
}

testNegative()
{
	NAME=$1
	DATA=$2
	if java -jar build/libs/app-cli.jar <<<"$DATA" >/dev/null; then
		echo -e "Test: $NAME -- FAILED\n"
		FAILED=$((FAILED + 1))
	else
		echo -e "Test: $NAME -- SUCCESS\n"
	fi
}

FAILED=0

mkdir -p func_tests/out/

OIFS=$IFS
IFS=$'\n'
# run positive
for TEST in $(jq -c '.tests[] | select(.positive)' ./func_tests/tests.json); do
	NAME=$(jq -r '.name' <<<"$TEST")
	DATA=$(jq -r '"\(.points_count) " + "\(.points | join(" ") )"' <<<"$TEST")
	DESC=$(jq -r '.desc | join("\n")' <<<"$TEST")
	testPositive "$NAME" "$DATA" "$DESC"
done

# run negative
for TEST in $(jq -c '.tests[] | select(.positive|not)' ./func_tests/tests.json); do
	NAME=$(jq -r '.name' <<<"$TEST")
	DATA=$(jq -r '"\(.points_count) " + "\(.points | join(" ") )"' <<<"$TEST")
	DESC=$(jq -r '.desc | join("\n")' <<<"$TEST")
	testNegative "$NAME" "$DATA" "$DESC"
done

IFS=$OIFS

exit $FAILED
