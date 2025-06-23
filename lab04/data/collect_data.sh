#!/bin/bash

CANVAS_WIDTH=1000
CANVAS_HEIGHT=1000

CENTER_X=500
CENTER_Y=500

COLOR_R=0
COLOR_G=0
COLOR_B=0

COUNT_START=50
COUNT_STEP=50
COUNT_END=450

ALGOS=("GENERAL" "PARAM")

DATA="data/data.txt"
EXEC="./build/image/bin/app-cli"
TEMP="data/temp.json"

create_test()
{
	jq -n \
		--argjson canvas "[$CANVAS_WIDTH, $CANVAS_HEIGHT]" \
		--argjson center "[$CENTER_X, $CENTER_Y]" \
		--argjson color "[$COLOR_R, $COLOR_G, $COLOR_B]" \
		--argjson ab "[$2, $2]" \
		--argjson count "1000" \
		--argjson points "$(($2 * 6))" \
		--arg algo "$1" '$ARGS.named' >"$TEMP"
}

test()
{
	echo -n "$2 $3 " >>"$DATA"
	jq -r '.elTime' <<<"$($EXEC "$1")" >>"$DATA"
}

for ((COUNT = COUNT_START; COUNT <= COUNT_END; COUNT += COUNT_STEP)); do
	for ALG in "${ALGOS[@]}"; do
		create_test "$ALG" "$COUNT"
		test "$TEMP" "$ALG" "$COUNT"
	done
done

rm -f "$TEMP"
