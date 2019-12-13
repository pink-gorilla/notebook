#!/bin/bash
curl 'http://localhost:3449/save' \
     -H 'Connection: keep-alive' \
     -H 'Pragma: no-cache' \
     -H 'Cache-Control: no-cache' \
     -H 'Accept: application/transit+json' \
     -H 'Origin: http://www.pinkgorilla.org' \
     -H 'User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36' \
     -H 'Content-Type: application/x-www-form-urlencoded;charset=UTF-8' \
     -H 'Referer: http://www.pinkgorilla.org/worksheet.html' \
     -H 'Accept-Encoding: gzip, deflate' \
     -H 'Accept-Language: de,en;q=0.9,de-DE;q=0.8,en-DE;q=0.7,en-US;q=0.6' \
     -H 'Cookie: ring-session=acb590c4-5eb1-40ac-991f-9f08b734f93e' \
     --data 'worksheet-filename=foobar.clj&worksheet-data=%3B%3B%20gorilla-repl.fileformat' \
     --compressed \
     --insecure
