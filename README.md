# elastic-tester
Tool for testing elastic search instance.

# Program Arguments
elastic-search-instance-url index doc-type

# Sample JSON document (index = dev & doc-type = article)
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

# Output
JEST Search raw JSON Object: {"_shards":{"total":1,"successful":1,"failed":0},"hits":{"total":1,"max_score":0.2876821,"hits":[{"_index":"dev","_type":"article","_id":"AV4BGPkj8PbXmg_W2fEm","_score":0.2876821,"_source":{"authour":"elastic","content":"JEST","@timestamp":"2017-08-20T15:13:51.982-04:00"}}]},"took":1,"timed_out":false}
{"_shards":{"total":1,"successful":1,"failed":0},"hits":{"total":1,"max_score":0.2876821,"hits":[{"_index":"dev","_type":"article","_id":"AV4BGPkj8PbXmg_W2fEm","_score":0.2876821,"_source":{"authour":"elastic","content":"JEST","@timestamp":"2017-08-20T15:13:51.982-04:00"}}]},"took":1,"timed_out":false}
JEST Search model hit: Article [authour=elastic, content=JEST, timestamp=2017-08-20T15:13:51.982-04:00]
Elastic hits: Article [authour=elastic, content=JEST, timestamp=2017-08-20T15:13:51.982-04:00]
