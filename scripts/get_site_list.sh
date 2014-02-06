#!/bin/bash

LIST="a b c d e f g h i j k l m n o p q r s t u v w x y z"

get_files() {
rm -rf SITES
mkdir SITES
for i in $LIST
do
	wget http://www.cyber-emploi-centre.com/site/1_emploi/emp_fr_liste_sites_bas_${i}.htm -O SITES/${i}.html
done
}

#get_files

rm /tmp/output
for i in $LIST
do
    iconv -f ISO-8859-1 -t UTF-8 < SITES/$i.html > SITES/$i.html8
	cat SITES/$i.html8  |
	sed 's/.*<tr>.*/===START===/'| 
	sed 's/.*<\/tr>.*/===END===/'| 
	sed 's/.*<td width="400" valign="top"[ =_a-z"0-9-]*>/===DESC===\n/' |
	sed 's/<a.*href="\([\.a-zA-Z0-9:_\/-]*\)"[ =_a-z"0-9-]*>/___\1___\n===NAME===\n/'    | 
	sed 's/<[^>]*>//g' | 
	sed 's/&nbsp;//g' >> /tmp/output
done

cat /tmp/output | perl site_list.pl