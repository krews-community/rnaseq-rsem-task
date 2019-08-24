#!/bin/bash

set -e

# cd to project root directory
cd "$(dirname "$(dirname "$0")")"

docker build --target base -t genomealmanac/rnaseq-rsemquant-base .

docker run --name rnaseq-rsemquant-base --rm -i -t -d \
    -v /tmp/rnaseq-test:/tmp/rnaseq-test \
    genomealmanac/rnaseq-rsemquant-base /bin/sh