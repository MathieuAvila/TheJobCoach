use strict; 
use warnings; 

sub  trim 
{ 
my $s = shift; 
$s =~ s/^\s+|\s+$//g; 
$s =~ s/\s+/ /g; 
$s =~ s/&amp;/&/g; 
return $s;
};



my %zones = 
(
"Alsace" => 'Arrays.asList("alsace")',
"Aquitaine" => 'Arrays.asList("aquitaine")',
"Auvergne" => 'Arrays.asList("auvergne")',
"Basse Normandie" => 'Arrays.asList("bassenormandie")',
"Basse-Normandie" => 'Arrays.asList("bassenormandie")',
"Basse-Normandie, Bretagne, Pays de la Loire et Poitou-Charentes" => 'Arrays.asList("bassenormandie", "bretagne", "paysdelaloire", "poitoucharentes")',
"Bourgogne" => 'Arrays.asList("bourgogne")',
"Bretagne" => 'Arrays.asList("bretagne")',
"Centre" => 'Arrays.asList("centre")',
"Champagne-Ardenne" => 'Arrays.asList("champagneardenne")',
"Dom-Tom" => 'Arrays.asList("domtom")',
"DOM-TOM" => 'Arrays.asList("domtom")',
"fc" => 'Arrays.asList("franchecomte")',
"Haute Normandie" => 'Arrays.asList("hautenormandie")',
"Languedoc Roussillon" => 'Arrays.asList("languedocroussillon")',
"Languedoc-Roussillon" => 'Arrays.asList("languedocroussillon")',
"Limousin" => 'Arrays.asList("limousin")',
"Lorraine" => 'Arrays.asList("lorraine")',
"midipir" => 'Arrays.asList("midipyrenees")',
"Nord-Pas de Calais" => 'Arrays.asList("nord")',
"PACA" => 'Arrays.asList("paca")',
"Pays de la Loire" => 'Arrays.asList("paysdelaloire")',
"Pays-de-la Loire" => 'Arrays.asList("paysdelaloire")',
"Pays-de-la-Loire" => 'Arrays.asList("paysdelaloire")',
"Picardie" => 'Arrays.asList("picardie")',
"Poitou-Charentes" => 'Arrays.asList("poitoucharentes")',
"paca" => 'Arrays.asList("paca")',
"Provence-Alpes-C�te d'azur" => 'Arrays.asList("paca")',
"Provence-Alpes-C�te d'Azur" => 'Arrays.asList("paca")',
"Rhone Alpes" => 'Arrays.asList("rhonealpes")',
"Rhone-Alpes" => 'Arrays.asList("rhonealpes")',
"Voir :" => 'Arrays.asList("")',
);

my %sectors = (
"Alim., Agric., Envir." => 'Arrays.asList("alimentation","agriculture", "environnement")',
"Alim., Agriculture, Environ." => 'Arrays.asList("alimentation","agriculture", "environnement")',
"Art, Médias, Communication" => 'Arrays.asList("art", "media", "communication")',
"Arts, Médias, Com." => 'Arrays.asList("art", "media", "communication")',
"Arts, Médias, Communication" => 'Arrays.asList("art", "media", "communication")',
"Arts, Médias, Communication Commerce, Distribution" => 'Arrays.asList("art", "media", "communication", "commerce", "distribution")',
"Banque, Assurance, Finance" => 'Arrays.asList("banque", "finance", "assurance")',
"Banque, Assur., Finance" => 'Arrays.asList("banque", "finance", "assurance")',
"BTP, Immo." => 'Arrays.asList("btp", "immobilier")',
"BTP, Immobilier" => 'Arrays.asList("btp", "immobilier")',
"BTP, Immobilier Electric., Electron., Meca." => 'Arrays.asList("btp", "immobilier", "electricite", "electronique", "mecanique")',
"BTP, Immobilier Electric., Electron., Méca." => 'Arrays.asList("btp", "immobilier", "electricite", "electronique", "mecanique")',
"Chimie, Sciences" => 'Arrays.asList("chimie", "sciences")',
"Commerce" => 'Arrays.asList("commerce")',
"Commerce, Distribution" => 'Arrays.asList("commerce", "distribution")',
"Electric., Electron., Méca." => 'Arrays.asList("electricite", "electronique", "mecanique")',
"Electricite, Electro., Méca." => 'Arrays.asList("electricite", "electronique", "mecanique")',
"Electricite, Electronique, Mécanique" => 'Arrays.asList("electricite", "electronique", "mecanique")',
"Humanitaire, Santé, Social" => 'Arrays.asList("humanitaire", "sante", "social")',
"Humanitaire, Sante, Social" => 'Arrays.asList("humanitaire", "sante", "social")',
"Humanit., Sante, Social" => 'Arrays.asList("humanitaire", "sante", "social")',
"Huma., Santé, Social" => 'Arrays.asList("humanitaire", "sante", "social")',
"Huma., Sante, Social" => 'Arrays.asList("humanitaire", "sante", "social")',
"Informatique, Télécom." => 'Arrays.asList("informatique", "telecoms")',
"Informatique, Télécoms" => 'Arrays.asList("informatique", "telecoms")',
"Loisirs, Tourisme, Restau." => 'Arrays.asList("loisirs", "tourisme", "restauration")',
"Matières premières" => 'Arrays.asList("matierespremieres")',
"Mode, Habillement, Soin du corps." => 'Arrays.asList("mode", "habillement", "soinsducorps")',
"Mode, Habil., Soin du corps" => 'Arrays.asList("mode", "habillement", "soinsducorps")',
"Mode, Habil, Soins" => 'Arrays.asList("mode", "habillement", "soinsducorps")',
"Mode, Habil., Soins" => 'Arrays.asList("mode", "habillement", "soinsducorps")',
"Services" => 'Arrays.asList("services")',
"Transports, Auto, Cycles" => 'Arrays.asList("transports", "automobiles", "cycles")',
"Transports, Auto., Cycles" => 'Arrays.asList("transports", "automobiles", "cycles")'
);


sub main
{
	my $url = "";
	my $type = "";
	my $name = "";
	my $cur = 0;
	while (<STDIN>) 	{
	my($line) = $_;
	if ( "$line" =~ "===END===") {
		#print "End: $line  -- $url\n";
		$name = trim($name);
		$type = trim($type);
		my $sector = "null";
		my $target = '"tous"';
		my $location = "null";
		my $ptype = "site";		

		#print $url . "===============" . $type . "\n";		
		
		if ( $type =~ /(.*)\/(.*)/ )
		{
			my $stype = $1;
			my $value = $2;		
			#print $stype . "============" . $value ." ===========\n";

			if ( "$stype" =~ ".*Interim.*" or "$url" =~ ".*interim.*" )
			{
				$ptype = "interim";
			} 
			elsif ( "$stype" =~ ".*Cab.*" )
			{
				$ptype = "cabinet";
			} 
			
			if ( "$type" =~ ".*Au pair.*" )
			{
				$sector = 'Arrays.asList("aupair")';
			}
			
			if ( "$type" =~ ".*Bac +2.*" )
			{
				$target = '"cadre"';
			}
			
			if ("$value" =~ "Par secteurs[ ]*\((.*)\)[ ]*")
			{
			 $sector = $1;
			 $sector =~ s/\(|\)//g;
			 
			 $sector =~ s/M.dia/Media/g;
			 $sector =~ s/lectricit./lectricite/g;
			 $sector =~ s/lectrici[^,]*,/lectricite,/g;
			 $sector =~ s/M.ca/Meca/g;
			 $sector =~ s/Sant../Sante/g;
			 $sector =~ s/T.l.com/Telecom/g;
			 $sector =~ s/Mati.res premi.res/Matieres premieres/g;
			 
			 #print "SECTOR====" . $sector . "\n";

			 if( not exists( $sectors{$sector} ) ) 
				{
   				print "ERROR==========$sector\n";	
				}					
		
			 $sector = $sectors{$sector};
 
			 
			 #print "SECTSUBST====" . $sector . "\n";
			 
			}
			 elsif ("$value" =~ "Par régions[ ]*\((.*)\)[ ]*")
			{
			 $location = $1;
			 $location =~ s/\(|\)//g;
			 $location =~ s/.*ne.Alpes/Rhone Alpes/g;
			 $location =~ s/.*zur/paca/g;
			 $location =~ s/Midi.*/midipir/g;
			 $location =~ s/Franche.*/fc/g;
			 #print "LOCATION == " . $location . "\n";
			 if( not exists( $zones{$location} ) ) 
				{
   				print "ERROR==========$location\n";	
				}					
		
			 $location = $zones{$location};
			 #print "LOCSUBST == " . $location . "\n";
			}
		 }
		 
		if ( "$url" ne "" )
		{
		  	print "siteList.add(new WebSiteDefinition($location,$target, $sector, \"$ptype\", \"$url\", \"" . $name . "\"));\n";
		}
		$url = "";
		$line = "";
		$type = "";
		$name = "";
		$cur = 0;
	}
	if ( "$line" =~ "===START===") {
		#print "Start: $line\n";
		#url = "";
		#type = "";
		$line = "";
	}
	if ( "$line" =~ m/.*___(.*)___.*/ ) 
	{ 
		if (( "$url" eq "") &&  ( "$1" ne "http://www.cyber-emploi-centre.com") )
		{
			$url = $1;
		}
		$line = "";
		#print "Yes $1\n"; 
	}
	$line =~ s/\r|\n//g;
	$line =~ s/[ ]+/ /g;
	if ( "$line" =~ m/.*===NAME===.*/ ) 
	{
	  $cur = 0;
	} 
	elsif ( "$line" =~ m/.*===DESC===.*/ ) 
	{
		$cur = 1;
	}
	elsif ( $cur == 0 )
	{
	   $name = $name . $line;
	}
	elsif ( $cur == 1 )
	{
	   $type = $type . $line;
	}
	#$type = $type . $line;
 }
}


main();
