cd `dirname "$0"`

version=`cat "./pom.xml" | grep -m 1 "<version>" | sed "s#</.*##" | sed "s#.*ion>##"`

#set path and version used in exe (must be in X.X.X.X format) depends on typed version
if [[ $version == *SNAPSHOT* ]]
then
	path="trunk"
	modifiedVersion="1.0.0.0"
else
	path="tags/"`basename ${PWD}`

	if [[ $version =~ .*\..*\..*\..* ]]
	then
		modifiedVersion="${version}"
	else 
		modifiedVersion="${version}.0"
	fi
fi

#build jar and prepare eclipse project to allow import project to eclipse for patch
mvn clean install package
cp ./target/Ancestry2html*.jar dist/Ancestry2html/lib/

#create exe wraper that runs built jar
cp release/launch4j.xml ./launch4j.xml
vi launch4j.xml -c "%s/{VERSION}/${version}/ge|%s/{MOD_VERSION}/${modifiedVersion}/ge|wq!"
launchXml="d:/Martin/Programing/Java/Ancestry2html/${path}/launch4j.xml"

"/cygdrive/c/Portable/launch4j/launch4jc.exe" $launchXml

#create sh script that runs built jar
cp release/Ancestry2html.sh ./dist/Ancestry2html/bin/Ancestry2html.sh
vi ./dist/Ancestry2html/bin/Ancestry2html.sh -c "%s/{VERSION}/${version}/ge|wq!"

#create setup exe
cp release/setup.iss ./setup.iss
vi setup.iss -c "%s#{VERSION}#${version}#ge|%s#{PATH}#${path}#ge|wq!"
"/cygdrive/c/Program Files (x86)/Inno Setup 5/ISCC.exe" /Q ./setup.iss
cd dist
md5sum.exe Ancestry2html${version}setup.exe > Ancestry2html${version}setup.md5
cd ..

cd dist
zip Ancestry2html${version}.zip -r Ancestry2html -9 -x "*/.svn/*"
md5sum.exe Ancestry2html${version}.zip > Ancestry2html${version}.md5
cd ..

rm launch4j.xml
rm setup.iss

