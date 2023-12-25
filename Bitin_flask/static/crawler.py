import requests
from bs4 import BeautifulSoup as bs
import os
import subprocess


def getArticle(coinName):
    ret = []
    curl = """curl 'https://kr.investing.com/search/?q=""" + coinName + """&tab=news' \
    -H 'authority: kr.investing.com' \
    -H 'accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7' \
    -H 'accept-language: en-US,en;q=0.9,ko;q=0.8' \
    -H 'cache-control: max-age=0' \
    -H 'cookie: gcc=KR; gsc=11; udid=d44c1accc7cbdab54a760e2d430a0070; smd=d44c1accc7cbdab54a760e2d430a0070-1701924745; invpro_promote_variant=0; __cflb=02DiuF9qvuxBvFEb2q9Qemd3EPFFTD8SAG7Q2jwMNGv52; pm_score=clear; user-browser-sessions=1; browser-session-counted=true; adBlockerNewUserDomains=1701924749; _hjFirstSeen=1; _hjIncludedInSessionSample_174945=0; _hjSession_174945=eyJpZCI6ImU3OGE3Mjk3LTRmNDgtNDRlZi1hMDRlLTM1NTEyNzE5MWYyZiIsImNyZWF0ZWQiOjE3MDE5MjQ3NTA2MjUsImluU2FtcGxlIjpmYWxzZSwic2Vzc2lvbml6ZXJCZXRhRW5hYmxlZCI6ZmFsc2V9; _hjAbsoluteSessionInProgress=0; _hjHasCachedUserAttributes=true; _gid=GA1.2.400916456.1701924754; _fbp=fb.1.1701924753991.910664191; _imntz_error=0; _pbjs_userid_consent_data=3524755945110770; im_sharedid=350038cf-5d6e-4d95-bfe0-dfc705a505b9; _cc_id=d946858e4a928bc4f385b7caa5441bed; panoramaId_expiry=1702529555859; panoramaId=928bab71e75c3347fe45d7c0cd8716d53938cd40f1ef765b6a0c11bfb9f5a3cf; panoramaIdType=panoIndiv; PHPSESSID=e93qiudk4qbu33v53qrunnog5f; geoC=KR; gtmFired=OK; adsFreeSalePopUp=3; lifetime_page_view_count=2; _hjSessionUser_174945=eyJpZCI6IjA2MGI2MzMyLWIzM2MtNTIyYi1iNjJjLThmOGMwYjViNzYyOSIsImNyZWF0ZWQiOjE3MDE5MjQ3NTA2MTgsImV4aXN0aW5nIjp0cnVlfQ==; r_p_s_n=1; reg_trk_ep=exit popup banner; protectedMedia=2; pms={"f":2,"s":2}; __cf_bm=iRV0EybaOL7ZA0FtFwnJ0pwh6gPcaCoGnk6LbmqJDu4-1701925676-0-AYq7JN7OBTQNY1RGpZtCi0yuOx/buIbvO88LJcCV/AFg/ZZSq/hcTkV36XmCElKummwi6Glz/vaBotqm7Af00Jw=; invpc=7; _gat=1; _gat_allSitesTracker=1; nyxDorf=OT0xYGY4YyFkM2FuNGNkeDFiYTozN2Z6MzsyOA%3D%3D; dicbo_id=%7B%22dicbo_fetch%22%3A1701926226420%7D; page_view_count=7; _ga=GA1.1.318012573.1701924754; _ga_C4NDLGKVMK=GS1.1.1701924754.1.1.1701926227.56.0.0; lotame_domain_check=investing.com; __gads=ID=364c3ee9cae5dbd9:T=1701924775:RT=1701926230:S=ALNI_MYHwyQ2N_ZvglopwhtWRSgjfP1_Tw; __gpi=UID=00000ca663d6a68a:T=1701924775:RT=1701926230:S=ALNI_Mbh-VhMUrIpXEoZffTSMv5U-rePvw' \
    -H 'sec-ch-ua: "Chromium";v="119", "Not?A_Brand";v="24"' \
    -H 'sec-ch-ua-mobile: ?0' \
    -H 'sec-ch-ua-platform: "Linux"' \
    -H 'sec-fetch-dest: document' \
    -H 'sec-fetch-mode: navigate' \
    -H 'sec-fetch-site: none' \
    -H 'sec-fetch-user: ?1' \
    -H 'upgrade-insecure-requests: 1' \
    -H 'user-agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36' \
    --compressed
    """

    out = subprocess.check_output(curl, shell=True)
    out = (out.decode("utf-8"))

    html3 = bs(out, 'lxml')
    titleList = html3.select('.searchSectionMain .title')

    for title in titleList:
        tmp = bs(str(title), "lxml").select_one(".title").text
        if (tmp == "{{title}}"):
            continue
        ret.append(tmp)
    return ret

# articles = getArticle("ethereum")
# res = '\n'.join(articles)
#
# print(res)