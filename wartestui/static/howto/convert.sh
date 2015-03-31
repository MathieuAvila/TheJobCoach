LEFT=10

INTER=4
SECTION_SIZE=$((160-135))

section()
{
    START=$1
    COUNT=$2
    LAST_PIXEL_RIGHT=$3
    WIDTH=$(( $LAST_PIXEL_RIGHT - $LEFT ))
    echo ${WIDTH}x$(( ${SECTION_SIZE} + 3 ))+${LEFT}+$(( $START + $COUNT * ( $INTER + $SECTION_SIZE ) ))
}

convert -extract  $( section 135 0 190 ) panel.jpg tasks.jpg
convert -extract  $( section 135 1 200 ) panel.jpg addresses.jpg
convert -extract  $( section 135 2 172 ) panel.jpg sites.jpg
convert -extract  $( section 135 3 155 ) panel.jpg documents.jpg
convert -extract  $( section 135 4 168 ) panel.jpg opportunities.jpg

convert -extract  $( section 344 0 128 ) panel.jpg preferences.jpg
convert -extract  $( section 344 1 136 ) panel.jpg reports.jpg
convert -extract  $( section 344 2 137 ) panel.jpg goals.jpg

convert -extract  $( section 496 0 165 ) panel.jpg connections.jpg
convert -extract  $( section 496 1 137 ) panel.jpg comments.jpg
convert -extract  $( section 496 2 82  ) panel.jpg news.jpg
convert -extract  $( section 496 3 191 ) panel.jpg library.jpg

convert -extract  $( section 42 1 77 ) panel_account.jpg help.jpg

