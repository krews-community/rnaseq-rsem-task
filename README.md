# RSEM task for RNA-seq

This task provides a convenience wrapper around [RSEM](https://bmcbioinformatics.biomedcentral.com/articles/10.1186/1471-2105-12-323). It computes gene and isoform quantifications given a BAM file of reads mapped to a transcriptome.

## Running

We encourage running this task as a Docker image, which is publicly available through GitHub packages. To pull the image, first [install Docker](https://docs.docker.com/engine/install/), then run
```
docker pull docker.pkg.github.com/krews-community/rnaseq-rsem-task/rnaseq-rsem:latest
```
The task requires a pre-built RSEM index to be provided as a tarball. The RSEM indexes available at ENCODE (for example [ENCFF940ABZ](https://www.encodeproject.org/files/ENCFF940AZB/)) are compatible with this task. Alternatively, you can package a RSEM index you have built into a compatible tarball by running
```
tar -cf index.tar -C /path/to/directory/with/index/files .
```
Then, to perform quantifications, simply run:
```
docker run \
    --volume /path/to/inputs:/input \
    --volume /path/to/outputs:/output \
    docker.pkg.github.com/krews-community/rnaseq-rsem-task/rnaseq-rsem:latest \
    java -jar /app/rsem.jar --bam /input/alignments.bam \
        --index /input/index.tar --output-directory /output
```
If your data are paired-end, run the above command with `--paired-end` added.

This will produce an output directory containing gene quantifications (`output.genes.results`) and isoform quantifications (`output.isoforms.results`).

### Parameters
This task supports several parameters:
|name|description|default|
|--|--|--|
|bam|BAM file containing alignments to a transcriptome|(required)|
|index|path to index tarball; may be gzipped or not|(required)|
|output-directory|directory in which to write output|(required)|
|paired-end|must be specified if reads are paired-end|false|
|strand|set if reads are from one strand; forward, reverse, or unstranded|unstranded|
|seed|random seed to use|(random)|
|output-prefix|prefix to use when naming output files|output|
|cores|number of cores available to the task|1|
|ram-gb|gigabytes of RAM available to the task|16|

## For developers

The task provides integrated unit testing, which quantifies a small number of reads aligned to a human mitochondrial index and checks that the outputs match expected values. To run the tests, first install Docker and Java, then clone this repo, then run
```
scripts/test.sh
```
Contributions to the code are welcome via pull request.
