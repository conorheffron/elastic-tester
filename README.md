# elastic-tester
 - Tool for testing elastic (ES, formerly elastic search) instance by storing no-sql JSON document by index and doc-type (to be setup manually in ES stack first).

![Auto Assign](https://github.com/conorheffron/elastic-tester/actions/workflows/auto-assign.yml/badge.svg)

[![Java CI with Maven](https://github.com/conorheffron/elastic-tester/actions/workflows/maven.yml/badge.svg)](https://github.com/conorheffron/elastic-tester/actions/workflows/maven.yml)

## Technologies Used
 - JDK 22, elasticsearch 8

# Program Arguments
elastic-search-instance-url index doc-type

# Sample JSON document (index = dev & doc-type = article)
```
{
  "_index": "dev",
  "_type": "article",
  "_id": "AV4BGPkj8PbXmg_W2fEm",
  "_version": 1,
  "found": true,
  "_source": {
    "authour": "elastic",
    "content": "JEST",
    "@timestamp": "2017-08-20T15:13:51.982-04:00"
  }
}
```

# Output
```
JEST Search raw JSON Object: {"_shards":{"total":1,"successful":1,"failed":0},"hits":{"total":1,"max_score":0.2876821,"hits":[{"_index":"dev","_type":"article","_id":"AV4BGPkj8PbXmg_W2fEm","_score":0.2876821,"_source":{"authour":"elastic","content":"JEST","@timestamp":"2017-08-20T15:13:51.982-04:00"}}]},"took":1,"timed_out":false}
{"_shards":{"total":1,"successful":1,"failed":0},"hits":{"total":1,"max_score":0.2876821,"hits":[{"_index":"dev","_type":"article","_id":"AV4BGPkj8PbXmg_W2fEm","_score":0.2876821,"_source":{"authour":"elastic","content":"JEST","@timestamp":"2017-08-20T15:13:51.982-04:00"}}]},"took":1,"timed_out":false}
JEST Search model hit: Article [authour=elastic, content=JEST, timestamp=2017-08-20T15:13:51.982-04:00]
Elastic hits: Article [authour=elastic, content=JEST, timestamp=2017-08-20T15:13:51.982-04:00]
```
