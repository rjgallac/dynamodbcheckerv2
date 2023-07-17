# dynamodb and java testing

## issues
connecting to dynamodb on localhost needs ip address to be ip of machine and not localhost or 127.0.0.1


aws dynamodb list-tables --endpoint-url http://localhost:8000


aws dynamodb --endpoint-url http://localhost:8000 create-table \
--table-name Music \
--attribute-definitions \
AttributeName=Artist,AttributeType=S \
AttributeName=SongTitle,AttributeType=S \
--key-schema \
AttributeName=Artist,KeyType=HASH \
AttributeName=SongTitle,KeyType=RANGE \
--provisioned-throughput \
ReadCapacityUnits=5,WriteCapacityUnits=5 \
--table-class STANDARD


aws dynamodb --endpoint-url http://localhost:8000 put-item \
--table-name Music  \
--item \
'{"Artist": {"S": "No One You Know"}, "SongTitle": {"S": "Call Me Today"}, "AlbumTitle": {"S": "Somewhat Famous"}, "Awards": {"N": "1"}}'

aws dynamodb --endpoint-url http://localhost:8000 put-item \
--table-name Music  \
--item \
'{"Artist": {"S": "No One You Know1"}, "SongTitle": {"S": "Call Me Today"}, "AlbumTitle": {"S": "Somewhat Famous"}, "Awards": {"N": "1"}}'

aws dynamodb --endpoint-url http://localhost:8000 put-item \
--table-name Music  \
--item \
'{"Artist": {"S": "No One You Know2"}, "SongTitle": {"S": "Call Me Today"}, "AlbumTitle": {"S": "Somewhat Famous"}, "Awards": {"N": "1"}}'


aws dynamodb --endpoint-url http://localhost:8000 scan --table-name Music