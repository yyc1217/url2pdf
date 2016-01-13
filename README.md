# url2pdf
A simple service that convert target url to pdf via http or https protocol by invoking [wkhtmltopdf](http://wkhtmltopdf.org), it's stateless, horizontal scalable.

## Prerequists

### wkhtmltopdf
You have to install wkhtmltopdf first, otherwise url2pdf won't startup successful. Follow the instructions from [wkhtmltopdf](http://wkhtmltopdf.org) based on your operating system.

### java 1.8
This service is developed under java 1.8.

## Running
Execute ``./gradlew build`` at command line interface, then packaged war file will be build/libs, deploy the war file to any java container, i.e. Tomcat, JBoss.

Or just ``./gradlew bootRun``.

## Usages
```{hostname}/url2pdf?target=www.google.com.tw```


There are two possible usages:

1. **proxy**: Invoking url above programmatically via http or https protocol, read converted result from service response's body, then send back to clients.
2. **direct**: Add a link tag in html, make clients invoking url above from their browser directly, see [Abuse Prevention](#abuse-prevention--privacy)

## Query Parameters
| Name        | Enums                                                                                         | Example           | Required | Default    | wkhtmltopdf option |
|-------------|-----------------------------------------------------------------------------------------------|-------------------|----------|------------|--------------------|
| target      |                                                                                               | www.google.com.tw | yes      |            |                    |
| filename    |                                                                                               | converted.pdf     | no       | result.pdf |                    |
| orientation | [LANDSCAPE, PORTRAILT]                                                                        | PORTRAILT         | no       | PORTRAILT  | --orientation      |
| pageSize    | [A0, A1, A2, A3, A4, A5, A6, A7, A8, A9, B0, B1, B2, B3, B4, B5, B6, B7, B8, B9, B10, LETTER] | B3                | no       | A4         | --page-size        |
| viewport    |                                                                                               | 1280x1024         | no       | 800x600    | --viewport-size    |
> If you want to develop more wkhtmltopdf options, see [wkhtmltopdf.txt](http://wkhtmltopdf.org/usage/wkhtmltopdf.txt) for more details.

## Restrict IP
**url2pdf** is not for public purpose but as a organization microservice, so please specify at least one ip prefix of your organization in [accept.target.ips.txt](src/main/resources/accept.target.ips.txt). 

**url2pdf** will reject any request by default if it can't find any specify ip prefixes.

## Abuse Prevention & Privacy
It's fine if you let your users convert a public page by their browser directly, but it may cause abuse issues from malicious users. Or you want your users convert a private page which needs some authentications before access. One possible solution is [Hash-based_message_authentication_code](https://en.wikipedia.org/wiki/Hash-based_message_authentication_code).


Simply says, your service should **sign** the requested url, give it to your users, then your verify the **signed** request from url2pdf to make sure it is permitted by your service.


For example, you want your users get a pdf file by converting http://target-service.com/private-page.html, so we will have
```
url2pdf.com/url2pdf?target=http://target-service.com/private-page.html&viewport=1280x1024&timestamp=1452609375
```
target service should **sign** this url by hmac function from its private key, append calculated hash:
> 2a1d7c2f78783913492e86730a4aaf1c9a71033e = hmac(target, viewport, timestamp, private key)

```
url2pdf.com/url2pdf?target=http://target-service.com/private-page.html&viewport=1280x1024&timestamp=1452609375&**hash=2a1d7c2f78783913492e86730a4aaf1c9a71033e**
```
target service should verify request from url2pdf invoked by users by repeat hmac function above, check calculated hash is match the hash from request.
> Of course, in this scenario private-page.html should allow any ip to access.

## Fonts
It might render incorrectly or blank due to lack of fonts required by html(i.e. Chinese, Japanese or special sympols). Add required fonts to OS or using css web fonts are two possible solutions.

## Acknowledge
This url2pdf idea is from an article [Spring From the Trenches: Creating PDF Documents With Wkhtmltopdf](http://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-creating-pdf-documents-with-wkhtmltopdf).
