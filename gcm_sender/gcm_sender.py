#!/usr/bin/env python
# -*- coding: utf-8 -*-
import urllib2
import json

url = 'https://android.googleapis.com/gcm/send'
apiKey = 'AIzaSyBA8J2UFXlUtMio9tEn-O1KPCy1xiu63Wg'
myKey = "key=" + apiKey
regid = 'dvRV47CQn88:APA91bG_NjCpu88A_DSvxI3KZ5-jbSzKyhkZfrTGvXn0L_mJBPaDWoWslEYyE7tDiD7TODTt4LRqZVmUabGj2Cy959T_CmAwInltvrP8pU1vjpfQCoHHBKB_K0WtKC--6959IttsDIUc'



# make header
headers = {'Content-Type': 'application/json', 'Authorization': myKey}

# make json data
data = {}
data['registration_ids'] = (regid,)
data['data'] = {'collapse_key':'do_not_collapse',
				#'from':'637663484755',
				'badge':'1',
				'message':'0 골 0 슈팅 0 어시스트 1 따봉',
				'fttcmd':'1',
				'title':'박주영 러시아전 기록',
				'imagesrc':'https://fbcdn-sphotos-c-a.akamaihd.net/hphotos-ak-xpf1/t1.0-9/s526x296/10308883_667138090021044_2398823190813603289_n.jpg',
				'style':'bc:#FFFF0000;tc:#FFFF00FF',
				'sound':'alarm1'}
json_dump = json.dumps(data)
# print json.dumps(data, indent=4)

def main():
	req = urllib2.Request(url, json_dump, headers)
	result = urllib2.urlopen(req).read()
	print json.dumps(result)

if __name__ == '__main__':
     main()