# FIMO-output-Analyzer
This is a program I wrote to automate a part of a project for the course Bprgu.
The program makes a selection from the possible TFBS from a FIMO output file.
It does this by comparing the positions of the TFBS with the positions of genes to filter out TFBS that overlap with or are further than 300 bp removed from a gene. The program uses a GenBank file to get the positions and names of the genes.


How to use:
Download the FIMO results as text file and download the GenBank file of the genome you analysed using femo. Drag and drop the files to "+ FIMO file" and "+ GenBank file" respectivly if the file is excepted the '+' wil become green if there is a problem with the file the '+' will become red. If both files are excepted the seletion of TFBS will be shown in the textfield.
