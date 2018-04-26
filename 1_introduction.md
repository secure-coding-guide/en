Introduction
============

Building a Secure Smartphone Society
------------------------------------

This guidebook is a collection of tips concerning the know-how of
secure designs and secure coding for Android application developers.
Our intent is to have as many Android application developers as
possible take advantage of this, and for that reason we are making it
public.

In recent years, the smartphone market has witnessed a rapid
expansion, and its momentum seems unstoppable. Its accelerated growth
is brought on due to the diverse range of applications. An unspecified
large number of key functions of mobile phones that were once not
accessible due to security restrictions on conventional mobile phones
have been made open to smartphone applications. Subsequently, the
availability of varied applications that were once closed to
conventional mobile phones is what makes smartphones more attractive.

With great power that comes from smartphone applications comes great
responsibility from their developers. The default security
restrictions on conventional mobile phones had made it possible to
maintain a relative level of security even for applications that were
developed without security awareness. As it has been aforementioned
with regard to smartphones, since the key advantage of a smartphone is
that they are open to application developers, if the developers design
or code their applications without the knowledge of security issues
then this could lead to risks of users\' personal information leakage
or exploitation by malware causing financial damage such as from
illicit calls to premium-rate numbers.

Due to Android being a very open model allowing access to many
functions on the smartphone, it is believed that Android application
developers need to take more care about security issues than iOS
application developers. In addition, responsibility for application
security is almost solely left to the application developers. For
example, applications can be released to the public without any
screening from a marketplace such as Google Play (former Android
Market), though this is not possible for iOS applications.

In conjunction with the rapid growth of the smartphone market, there
has been a sudden influx of software engineers from different areas in
the smartphone application development market. As a result, there is
an urgent call for the sharing knowledge of secure design and
consolidation of secure coding know-how for specific security issues
related to mobile applications.

Due to these circumstances, Japan\'s Smartphone Security Association
(JSSEC) has launched the Secure Coding Group, and by collecting the
know-how of secure design as well as secure coding of Android
applications, it has decided to make all of the information public
with this guidebook. It is our intention to raise the security level
of many of the Android applications that are released in the market by
having many Android application developers become acquainted with the
know-how of secure design and coding. As a result, we believe we will
be contributing to the creation of a more reliable and safe smartphone
society.

Timely Feedback on a Regular Basis Through the Beta Version
-----------------------------------------------------------

We, the JSSEC Secure Coding Group, will do our best to keep the
content contained in the Guidebook as accurate as possible, but we
cannot make any guarantees. We believe it is our priority to publicize
and share the know-how in a timely fashion. Equally, we will upload
and publicize what we consider to be the latest and most accurate
correct information at that particular juncture, and will update it
with more accurate information once we receive any feedback or
corrections. In other words, we are taking the beta version approach
on a regular basis. We think this approach would be meaningful for
many of the Android application developers who are planning on using
the Guidebook.

The latest version of the Guidebook and sample codes can be obtained
from the URL below.

-   [http://www.jssec.org/dl/android_securecoding_en.pdf](http://www.jssec.org/dl/android_securecoding_en.pdf)
    Guidebook (English)

-   [http://www.jssec.org/dl/android_securecoding_en.zip](http://www.jssec.org/dl/android_securecoding_en.zip)
    Sample Codes (English)

The latest Japanese version can be obtained from the URL below.

-   [http://www.jssec.org/dl/android_securecoding.pdf](http://www.jssec.org/dl/android_securecoding.pdf)
    Guidebook (Japanese)

-   [http://www.jssec.org/dl/android_securecoding.zip](http://www.jssec.org/dl/android_securecoding.zip)
    Sample Codes (Japanese)

Usage Agreement of the Guidebook
--------------------------------

We need your consent for the following two precautionary statements
when using the Guidebook.

1.  The information contained in the Guidebook may be inaccurate. Please
    use the information written here by your own discretion.

2.  In case of finding any mistakes contained in the Guidebook, please
    send us an e-mail to the address listed below. However, we cannot
    guarantee a reply or any revisions thereof.

Japan Smartphone Security Association

Secure Coding Group Inquiry

E-mail:
[jssec-securecoding-qa@googlegroups.com](mailto:jssec-securecoding-qa@googlegroups.com)

Subject: [Comment] Android Secure Coding Guidebook 20180201EN

Content: Name (optional), Affiliation (optional), E-mail (optional),
Comment (required) and Other matters (optional)

Correction articles of February 1, 2018 edition
-----------------------------------------------

This section provides a list of corrections and modifications for the
previous edition from the viewpoint of security, as a result of
further studies.

In correcting articles, we adopted the outcomes of our studies and the
valuable opinions of those who read the former editions of this
guidebook.

Especially, taking in readers\' opinions is considered as a key factor
in making the document highly practical.

We recommend, for those who use a previous edition of the document as
a reference, taking a look at the list below. Note that the list does
not include the following kinds of changes and error corrections:
fixes of typos, new articles added in this edition, organizational
changes, and improvements in expression.

Any comments, opinions or suggestions on this guidebook are greatly
appreciated.

**List of revisions**

<table border="yes" bordercolor="gray">
<thead bgcolor="lightgray">
<tr>
<th width="30%">Section revised in 2/1/2018 version</th>
<th width="30%">Section revised in this version</th>
<th width="40%">Revision</th>
</tr>
</thead>
<tbody>
<tr>
<td>(not applicable)</td>
<td>4.1.3.7 The Autofill framework</td>
<td>Added a description of the Autofill framework.</td>
</tr>
<tr>
<td>4.2 Receiving/Sending Broadcasts</td>
<td>4.2 Receiving/Sending Broadcasts</td>
<td>Added a discussion of restrictions on the receipt of implicit Broadcast Intents in Android 8.0(API Level 26) and later.</td>
</tr>
<tr>
<td>5.2.3.6 Modifications to the Permission model specifications in Android versions 6.0 and later</td>
<td>5.2.3.6 Modifications to the Permission model specifications in Android versions 6.0 and later</td>
<td>Added a discussion of modifications to behavior regarding the granting of Permissions in Android 8.0 (API Level 26) and later.</td>
</tr>
<tr>
<td>5.3.2.4 Provide KEY_INTENT with Explicit Intent with the Specified Class Name of Login Screen Activity (Required</td>
<td>5.3.2.4 Provide KEY_INTENT with Explicit Intent with the Specified Class Name of Login Screen Activity (Required</td>
<td>Added a discussion of changes in the behavior of Key Intents before and after Android 4.4 (API Level 19）.</td>
</tr>
<tr>
<td>5.3.2.6 Password Should Not Be Saved in Account Manager (Recommended</td>
<td>5.3.2.6 Password Should Not Be Saved in Account Manager (Recommended</td>
<td>Added a description of password storage locations for Android 7.0 (API Level 24) and later.</td>
</tr>
<tr>
<td>5.3.3.1 Usage of Account Manager and Permission</td>
<td>5.3.3.1 Usage of Account Manager and Permission</td>
<td>Added a discussion of support for Permissions and methods related to AccountManager in Android 6.0 (API Level 23) and later and Android 8.0 (API Level 26) and later.</td>
</tr>
<tr>
<td>(not applicable)</td>
<td>5.3.3.3 Cases in which Authenticator accounts with non-matching signatures may be read in Android 8.0 (API Level 26) or later</td>
<td>For Android 8.0 (API Level 26) and later, added a discussion of cases in which it is possible to obtain account information for Authenticators with non-matching signatures, and how to handle such cases.</td>
</tr>
<tr>
<td>5.4 Communicating via HTTPS</td>
<td>5.4 Communicating via HTTPS</td>
<td>Added a discussion of the removal of support for SSLv3 in Android 8.0 and later.</td>
</tr>
<tr>
<td>5.4.3.1 How to Create Private Certificate and Configure Server Settings</td>
<td>5.4.3.1 How to Create Private Certificate and Configure Server Settings</td>
<td>Revised the creation script to include the SubjectAltName in the private certificate.</td>
</tr>
<tr>
<td>(not applicable)</td>
<td>(Column): Transitioning to TLS1.2 for secure connections</td>
<td>Added an article to recommend migration of HTTPS (SSL / TLS) communication to TLS 1.2.</td>
</tr>
<tr>
<td>(not applicable)</td>
<td>5.5.3.3 Version-dependent differences in handling of Android IDs</td>
<td>Added a discussion of version-dependent differences in values and generation rules for Android IDs.</td>
</tr>
</tbody>
</table>
