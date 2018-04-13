Using Technology in a Safe Way
==============================

In Android, there are many specific security related issues that
pertain only to certain technologies such as Activities or SQLite. If
a developer does not have enough knowledge about each of the different
security issues regarding each technology when designing and coding,
then unexpected vulnerabilities may arise. This chapter will explain
about the different scenarios that developers will need to know when
using their application components.

Creating/Using Activities
-------------------------

### Sample Code<!-- 6207eabf -->

The risks and countermeasures of using Activities differ depending on
how that Activity is being used. In this section, we have classified 4
types of Activities based on how the Activity is being used. You can
find out which type of activity you are supposed to create through the
following chart shown below. Since the secure coding best practice
varies according to how the activity is used, we will also explain
about the implementation of the Activity as well.

Table 4.1‑1 Definition of Activity Types
```eval_rst
=================== ===================================================================================================
Type                Definition
=================== ===================================================================================================
Private Activity    An activity that cannot be launched by another application, and therefore is the safest activity
Public Activity     An activity that is supposed to be used by an unspecified large number of applications.
Partner Activity    An activity that can only be used by specific applications made by a trusted partner company.
In-house Activity   An activity that can only be used by other in-house applications.
=================== ===================================================================================================
```

![](media/image34.png)
```eval_rst
.. {width="6.889763779527559in"
.. height="3.0074803149606297in"}
```

Figure 4.1‑1

#### Creating/Using Private Activities

Private Activities are Activities which cannot be launched by the
other applications and therefore it is the safest Activity.

When using Activities that are only used within the application
(Private Activity), as long as you use explicit Intents to the class
then you do not have to worry about accidently sending it to any other
application. However, there is a risk that a third party application
can read an Intent that is used to start the Activity. Therefore it is
necessary to make sure that if you are putting sensitive information
inside an Intent used to start an Activity that you take
countermeasures to make sure that it cannot be read by a malicious
third party.

Sample code of how to create a Private Activity is shown below.

Points (Creating an Activity):

1.  Do not specify taskAffinity.
2.  Do not specify launchMode.
3.  Explicitly set the exported attribute to false.
4.  Handle the received intent carefully and securely, even though the
    intent was sent from the same application.
5.  Sensitive information can be sent since it is sending and receiving
    all within the same application.

To make the Activity private, set the \"exported\" attribute of the
Activity element in the AndroidManifest.xml to false.

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Activity PrivateActivity.app.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```
PrivateActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Activity PrivateActivity.PrivateActivity.java
   :language: java
   :encoding: shift-jis
```

Next, we show the sample code for how to use the Private Activity.

```eval_rst
Point (Using an Activity):

6.  Do not set the FLAG\_ACTIVITY\_NEW\_TASK flag for intents to start
    an activity.
7.  Use the explicit Intents with the class specified to call an
    activity in the same application.
8.  Sensitive information can be sent only by putExtra() since the
    destination activity is in the same application. [1]_
9.  Handle the received result data carefully and securely, even though
    the data comes from an activity within the same application.

.. [1] Caution: Unless points 1, 2 and 6 are abided by, there is a risk that Intents may be read by a third party. Please refer to sections 4.1.2.2 and 4.1.2.3 for more details.
```

PrivateUserActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Activity PrivateActivity.PrivateUserActivity.java
   :language: java
   :encoding: shift-jis
```

#### Creating/Using Public Activities

Public Activities are Activities which are supposed to be used by an
unspecified large number of applications. It is necessary to be aware
that Public Activities may receive Intents sent from malware.

In addition, when using Public Activities, it is necessary to be aware
of the fact that malware can also receive or read the Intents sent to
them.

The sample code to create a Public Activity is shown below.

Points (Creating an Activity):

1. Explicitly set the exported attribute to true.
2. Handle the received intent carefully and securely.
3. When returning a result, do not include sensitive information.

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Activity PublicActivity.app.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```
PublicActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Activity PublicActivity.PublicActivity.java
   :language: java
   :encoding: shift-jis
```

Next, Herein after sample code of Public Activity user side.

```eval_rst
Points (Using an Activity):

4. Do not send sensitive information.
5. When receiving a result, handle the data carefully and securely.
```

PublicUserActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Activity PublicUser.PublicUserActivity.java
   :language: java
   :encoding: shift-jis
```

#### Creating/Using Partner Activities

Partner activities are Activities that can only be used by specific
applications. They are used between cooperating partner companies that
want to securely share information and functionality.

There is a risk that a third party application can read an Intent that
is used to start the Activity. Therefore it is necessary to make sure
that if you are putting sensitive information inside an Intent used to
start an Activity that you take countermeasures to make sure that it
cannot be read by a malicious third party

Sample code for creating a Partner Activity is shown below.

Points (Creating an Activity):

1. Do not specify taskAffinity.
2. Do not specify launchMode.
3. Do not define the intent filter and explicitly set the exported
   attribute to true.
4. Verify the requesting application\'s certificate through a
   predefined whitelist.
5. Handle the received intent carefully and securely, even though the
   intent was sent from a partner application.
6. Only return Information that is granted to be disclosed to a partner
   application.

Please refer to \"4.1.3.2 Validating the Requesting Application\" for
how to validate an application by a white list. Also, please refer to
\"5.2.1.3 How to Verify the Hash Value of an Application\'s
Certificate\" for how to verify the certificate hash value of a
destination application which is specified in the whitelist.

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Activity PartnerActivity.activityPartnerActivity.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```

PartnerActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Activity PartnerActivity.PartnerActivity.java
   :language: java
   :encoding: shift-jis
```

PkgCertWhitelists.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCertWhitelists.java
   :language: java
   :encoding: shift-jis
```

PkgCert.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCert.java
   :language: java
   :encoding: shift-jis
```

Sample code for using a Partner Activity is described below.

```eval_rst
Points (Using an Activity):

7. Verify if the certificate of the target application has been registered in a whitelist.
8. Do not set the FLAG\_ACTIVITY\_NEW\_TASK flag for the intent that start an activity.
9. Only send information that is granted to be disclosed to a Partner Activity only by putExtra().
10. Use explicit intent to call a Partner Activity.
11. Use startActivityForResult() to call a Partner Activity.
12. Handle the received result data carefully and securely, even though the data comes from a partner application.
```

Refer to \"4.1.3.2 Validating the Requesting Application\" for how to
validate applications by white list. Also please refer to \"5.2.1.3
How to Verify the Hash Value of an Application\'s Certificate\" for
how to verify the certificate hash value of a destination application
which is to be specified in a white list.

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Activity PartnerUser.activityPartnerUser.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```

PartnerUserActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Activity PartnerUser.PartnerUserActivity.java
   :language: java
   :encoding: shift-jis
```

PkgCertWhitelists.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCertWhitelists.java
   :language: java
   :encoding: shift-jis
```

PkgCert.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCert.java
   :language: java
   :encoding: shift-jis
```


#### Creating/Using In-house Activities

In-house activities are the Activities which are prohibited to be used
by applications other than other in-house applications. They are used
in applications developed internally that want to securely share
information and functionality.

There is a risk that a third party application can read an Intent that
is used to start the Activity. Therefore it is necessary to make sure
that if you are putting sensitive information inside an Intent used to
start an Activity that you take countermeasures to make sure that it
cannot be read by a malicious third party.

Sample code for creating an In-house Activity is shown below.

Points (Creating an Activity):

1.  Define an in-house signature permission.
2.  Do not specify taskAffinity.
3.  Do not specify launchMode.
4.  Require the in-house signature permission.
5.  Do not define an intent filter and explicitly set the exported attribute to true.
6.  Verify that the in-house signature permission is defined by an in-house application.
7.  Handle the received intent carefully and securely, even though the intent was sent from an in-house application.
8.  Sensitive information can be returned since the requesting application is in-house.
9.  When exporting an APK, sign the APK with the same developer key as the destination application.

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Activity InhouseActivity.activityInhouseActivity.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```

InhouseActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Activity InhouseActivity.InhouseActivity.java
   :language: java
   :encoding: shift-jis
```

SigPerm.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.SigPerm.java
   :language: java
   :encoding: shift-jis
```

PkgCert.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCert.java
   :language: java
   :encoding: shift-jis
```


\*\*\* Point9 \*\*\* When exporting an APK, sign the APK with the same developer key as the destination application.

![](media/image35.png)
```eval_rst
.. {width="4.647222222222222in"
.. height="3.2743055555555554in"}
```

Figure 4.1‑2

Sample code for using an In-house Activity is described below.

```eval_rst
Points (Using an activity):

10.  Declare that you want to use the in-house signature permission.
11.  Verify that the in-house signature permission is defined by an in-house application.
12.  Verify that the destination application is signed with the in-house certificate.
13.  Sensitive information can be sent only by putExtra() since the destination application is in-house.
14.  Use explicit intents to call an In-house Activity.
15.  Handle the received data carefully and securely, even though the data came from an in-house application.
16.  When exporting an APK, sign the APK with the same developer key as the destination application.
```

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Activity InhouseUser.activityInhouseUser.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```

InhouseUserActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Activity InhouseUser.InhouseUserActivity.java
   :language: java
   :encoding: shift-jis
```

SigPerm.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.SigPerm.java
   :language: java
   :encoding: shift-jis
```

PkgCert.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCert.java
   :language: java
   :encoding: shift-jis
```


\*\*\* Point 16 \*\*\* When exporting an APK, sign the APK with the same developer key as the destination application.

![](media/image35.png)
```eval_rst
.. {width="4.647222222222222in"
.. height="3.2743055555555554in"}
```

Figure 4.1‑3

### Rule Book<!-- 28ac098a -->

Be sure to follow the rules below when creating or sending an Intent to an activity.

1.  Activities that are Used Only Internally to the Application Must be
    Set Private (Required)

2.  Do Not Specify taskAffinity (Required)

3.  Do Not Specify launchMode (Required)

4.  Do Not Set the FLAG\_ACTIVITY\_NEW\_TASK Flag for Intents that Start
    an Activity (Required)

5.  Handling the Received Intent Carefully and Securely (Required)

6.  Use an In-house Defined Signature Permission after Verifying that it
    is Defined by an In-House Application (Required)

7.  When Returning a Result, Pay Attention to the Possibility of
    Information Leakage of that Result from the Destination Application
    (Required)

8.  Use the explicit Intents if the destination Activity is
    predetermined. (Required)

9.  Handle the Returned Data from a Requested Activity Carefully and
    Securely (Required)

10. Verify the Destination Activity if Linking with Another Company\'s
    Application (Required)

11. When Providing an Asset Secondhand, the Asset should be Protected
    with the Same Level of Protection (Required)

12. Sending Sensitive Information Should Be Limited as much as possible
    (Recommended)

#### Activities that are Used Only Internally to the Application Must be Set Private (Required)

Activities which are only used in a single application are not
required to be able to receive any Intents from other applications.
Developers often assume that Activities intended to be private will
not be attacked but it is necessary to explicitly make these
Activities private in order to stop malicious Intents from being
received.

AndroidManifest.xml

``` xml
        <!-- Private activity -->
        <!-- *** POINT 3 *** Explicitly set the exported attribute to false. -->
        <activity
            android:name=".PrivateActivity"
            android:label="@string/app_name"
            android:exported="false" />
```

Intent filters should not be set on activities that are only used in a
single application. Due to the characteristics of Intent filters, Due
to the characteristics of how Intent filters work, even if you intend
to send an Intent to a Private Activity internally, if you send the
Intent through an Intent filter than you may unintentionally start
another Activity. Please see Advanced Topics \"4.1.3.1Combining
Exported Attributes and Intent Filter Settings (For Activities)\" for
more details.

AndroidManifest.xml(Not recommended)

``` xml
        <!-- Private activity -->
        <!-- *** POINT 3 *** Explicitly set the exported attribute to false. -->
        <activity
            android:name=".PictureActivity"
            android:label="@string/picture_name"
            android:exported="false" >
            <intent-filter>
                <action android:name=”org.jssec.android.activity.OPEN />
            </intent-filter>
        </activity>
```
#### Do Not Specify taskAffinity (Required)

In Android OS, Activities are managed by tasks. Task names are
determined by the affinity that the root Activity has. On the other
hand, for Activities other than root Activities, the task to which the
Activity belongs is not determined by the Affinity only, but also
depends on the Activity\'s launch mode. Please refer to \"4.1.3.4 Root
Activity\" for more details.

In the default setting, each Activity uses its package name as its
affinity. As a result, tasks are allocated according to application,
so all Activities in a single application will belong to the same
task. To change the task allocation, you can make an explicit
declaration for the affinity in the AndroidManifest.xml file or you
can set a flag in an Intent sent to an Activity. However, if you
change task allocations, there is a risk that another application
could read the Intents sent to Activities belonging to another task.

Be sure not to specify android:taskAffinity in the AndroidManifest.xml
file and use the default setting keeping the affinity as the package
name in order to prevent sensitive information inside sent or received
Intents from being read by another application.

Below is an example AndroidManifest.xml file for creating and using
Private Activities.

AndroidManifest.xml

``` xml
    <!-- *** POINT 1 *** Do not specify taskAffinity -->
    <application
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name" >

        <!-- *** POINT 1 *** Do not specify taskAffinity -->
        <activity
            android:name=".PrivateUserActivity"
            android:label="@string/app_name" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- Private activity -->
        <!-- *** POINT 1 *** Do not specify taskAffinity -->
        <activity
            android:name=".PrivateActivity"
            android:label="@string/app_name"
            android:exported="false" />
    </application>
```
```eval_rst
Please refer to the \"Google Android Programming guide\"[2]_, the
Google Developer's API Guide \"Tasks and Back Stack\"[3]_, \"4.1.3.3
Reading Intents Sent to an Activity\" and \"4.1.3.4 Root Activity\"
for more details about tasks and affinities.

.. [2] Author Egawa, Fujii, Asano, Fujita, Yamada, Yamaoka, Sano,
    Takebata, "Google Android Programming Guide", ASCII Media Works,
    July 2009
.. [3] http://developer.android.com/guide/components/tasks-and-back-stack.html
```

#### Do Not Specify launchMode (Required)

The Activity launch mode is used to control the settings for creating
new tasks and Activity instances when starting an Activity. By default
it is set to \"standard\". In the \"standard\" setting, new instances
are always created when starting an Activity, tasks follow the tasks
belonging to the calling Activity, and it is not possible to create a
new task. When a new task is created, it is possible for other
applications to read the contents of the calling Intent so it is
required to use the \"standard\" Activity launch mode setting when
sensitive information is included in an Intent.

The Activity launch mode can be explicitly set in the
android:launchMode attribute in the AndroidManifest.xml file, but
because of the reason explained above, this should not be set in the
Activity declaration and the value should be kept as the default
\"standard\".

AndroidManifest.xml

``` xml
        <!-- *** POINT 2 *** Do not specify launchMode -->
        <activity
            android:name=".PrivateUserActivity"
            android:label="@string/app_name" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- Private activity -->
        <!-- *** POINT 2 *** Do not specify launchMode -->
        <activity
            android:name=".PrivateActivity"
            android:label="@string/app_name"
            android:exported="false" />
    </application>
```
Please refer to \"4.1.3.3 Reading Intents Sent to an Activity\" and
\"4.1.3.4 Root Activity.\"

#### Do Not Set the FLAG\_ACTIVITY\_NEW\_TASK Flag for Intents that Start an Activity (Required)

The launch mode of an Activity can be changed when executing
startActivity() or startActivityForResult() and in some cases a new
task may be generated. Therefore it is necessary to not change the
launch mode of Activity during execution.

To change the Activity launch mode, set the Intent flags by using
setFlags() or addFlags() and use that Intent as an argument to
startActivity() or startActivityForResult(). FLAG\_ACTIVITY\_NEW\_TASK
is the flag used to create a new task. When the
FLAG\_ACTIVITY\_NEW\_TASK is set, a new task will be created if the
called Activity does not exist in the background or foreground.

The FLAG\_ACTIVITY\_MULTIPLE\_TASK flag can be set simultaneously with
FLAG\_ACTIVITY\_NEW\_TASK. In this case, a new task will always be
created. New tasks may be created with either setting so these should
not be set with Intents that handle sensitive information.

Example of sending an intent
``` java
        Intent intent = new Intent();

        // *** POINT 6 *** Do not set the FLAG\_ACTIVITY\_NEW\_TASK flag for the intent to start an activity.

        intent.setClass(this, PrivateActivity.class);
        intent.putExtra("PARAM", "Sensitive Info");

        startActivityForResult(intent, REQUEST_CODE);
```

In addition, you may think that there is a way to prevent the contents
of an Intent from being read even if a new task was created by
explicitly setting the FLAG\_ACTIVITY\_EXCLUDE\_FROM\_RECENTS flag.
However, even by using this method, the contents can be read by a
third party so you should avoid any usage of
FLAG\_ACTIVITY\_NEW\_TASK.

Please refer to \"4.1.3.1Combining Exported Attributes and Intent
Filter Settings (For Activities)\" \"4.1.3.3 Reading Intents Sent to
an Activity\" and \"4.1.3.4 Root Activity.\"

#### Handling the Received Intent Carefully and Securely (Required)<!-- 97618625 -->

Risks differ depending on the types of Activity, but when processing a
received Intent data, the first thing you should do is input
validation.

Since Public Activities can receive Intents from untrusted sources,
they can be attacked by malware. On the other hand, Private Activities
will never receive any Intents from other applications directly, but
it is possible that a Public Activity in the targeted application may
forward a malicious Intent to a Private Activity so you should not
assume that Private Activities cannot receive any malicious input.
Since Partner Activities and In-house Activities also have the risk of
a malicious intent being forwarded to them as well, it is necessary to
perform input validation on these Intents as well.

Please refer to \"3.2 Handling Input Data Carefully and Securely\"

#### Use an In-house Defined Signature Permission after Verifying that it is Defined by an In-House Application (Required)<!-- 5261853e -->

Make sure to protect your in-house Activities by defining an in-house
signature permission when creating the Activity. Since defining a
permission in the AndroidManifest.xml file or declaring a permission
request does not provide adequate security, please be sure to refer to
\"5.2.1.2 How to Communicate Between In-house Applications with
In-house-defined Signature Permission.\"

####  When Returning a Result, Pay Attention to the Possibility of Information Leakage of that Result from the Destination Application (Required)<!-- 607bcc1f -->

When you use setResult() to return data, the reliability of the
destination application will depend on the Activity type. When Public
Activities are used to return data, the destination may turn out to be
malware in which case that information could be used in a malicious
way. For Private and In-house Activities, there is not much need to
worry about data being returned to be used maliciously because they
are being returned to an application you control. Partner Activities
are somewhat in the middle.

As above, when returning data from Activities, you need to pay
attention to information leakage from the destination application.

Example of returning data.
``` java
    public void onReturnResultClick(View view) {

        // *** POINT 6 *** Information that is granted to be disclosed to a partner application can be returned.
        Intent intent = new Intent();
        intent.putExtra("RESULT", "Information that is granted to disclose to partner applications");
        setResult(RESULT_OK, intent);
        finish();
    }
```

#### Use the explicit Intents if the destination Activity is predetermined. (Required)

When using an Activity by implicit Intents, the Activity in which the
Intent gets sent to is determined by the Android OS. If the Intent is
mistakenly sent to malware then Information leakage can occur. On the
other hand, when using an Activity by explicit Intents, only the
intended Activity will receive the Intent so this is much safer.

Unless it is absolutely necessary for the user to determine which
application\'s Activity the intent should be sent to, you should use
explicit intents and specify the destination in advance.

Using an Activity in the same application by an explicit Intent
``` java
        Intent intent = new Intent(this, PictureActivity.class);
        intent.putExtra("BARCODE", barcode);
        startActivity(intent);
```
Using other applicaion\'s Public Activity by an explicit Intent
``` java
        Intent intent = new Intent();
        intent.setClassName(
            "org.jssec.android.activity.publicactivity",
            "org.jssec.android.activity.publicactivity.PublicActivity");
        startActivity(intent);
```
However, even when using another application\'s Public Activity by
explicit Intents, it is possible that the destination Activity could
be malware. This is because even if you limit the destination by
package name, it is still possible that a malicious application can
fake the same package name as the real application. To eliminate this
type of risk, it is necessary to consider using a Partner or In-house.

Please refer to \"4.1.3.1Combining Exported Attributes and Intent
Filter Settings (For Activities)\"

#### Handle the Returned Data from a Requested Activity Carefully and Securely (Required)

While the risks differ slightly according to what type of Activity you
accessing, when processing Intent data received as a returned value,
you always need to perform input validation on the received data.

Public Activities have to accept returned Intents from untrusted
sources so when accessing a Public Activity it is possible that, the
returned Intents are actually sent by malware. It is often mistakenly
thought that all returned Intents from a Private Activity are safe
because they are originating from the same application. However, since
it is possible that an intent received from an untrusted source is
indirectly forwarded, you should not blindly trust the contents of
that Intent. Partner and In-house Activities have a risk somewhat in
the middle of Private and Public Activities. Be sure to input validate
these Activities as well.

Please refer to \"3.2 Handling Input Data Carefully and Securely\" for
more information.

####  Verify the Destination Activity if Linking with Another Company\'s Application (Required)

Be sure to sure a whitelist when linking with another company\'s
application. You can do this by saving a copy of the company\'s
certificate hash inside your application and checking it with the
certificate hash of the destination application. This will prevent a
malicious application from being able to spoof Intents. Please refer
to sample code section \"4.1.1.3 Creating/Using Partner Activities\"
for the concrete implementation method. For technical details, please
refer to \"4.1.3.2 Validating the Requesting Application.\"

#### When Providing an Asset Secondhand, the Asset should be Protected with the Same Level of Protection (Required)

When an information or function asset, which is protected by a
permission, is provided to another application secondhand, you need to
make sure that it has the same required permissions needed to access
the asset. In the Android OS permission security model, only an
application that has been granted proper permissions can directly
access a protected asset. However, there is a loophole because an
application with permissions to an asset can act as a proxy and allow
access to an unprivileged application. Substantially this is the same
as re-delegating a permission so it is referred to as the \"Permission
Re-delegation\" problem. Please refer to \"5.2.3.4 Permission
Re-delegation Problem.\"

#### Sending Sensitive Information Should Be Limited as much as possible (Recommended)<!-- 7f40888a -->

You should not send sensitive information to untrusted parties. Even
when you are linking with a specific application, there is still a
chance that you unintentionally send an Intent to a different
application or that a malicious third party can steal your Intents.
Please refer to \"4.1.3.5 Log Output When using Activities.\"

You need to consider the risk of information leakage when sending
sensitive information to an Activity. You must assume that all data in
Intents sent to a Public Activity can be obtained by a malicious third
party. In addition, there is a variety of risks of information leakage
when sending Intents to Partner or In-house Activities as well
depending on the implementation. Even when sending data to Private
Activities, there is a risk that the data in the Intent could be
leaked through LogCat. Information in the extras part of the Intent is
not output to LogCat so it is best to store sensitive information
there.

However, not sending sensitive data in the first place is the only
perfect solution to prevent information leakage therefore you should
limit the amount of sensitive information being sent as much as
possible. When it is necessary to send sensitive information, the best
practice is to only send to a trusted Activity and to make sure the
information cannot be leaked through LogCat.

In addition, sensitive information should never be sent to the root
Activity. Root Activities are Activities that are called first when a
task is created. For example, the Activity which is launched from
launcher is always the root Activity.

Please refer to \"4.1.3.3 Reading Intents Sent to an Activity\" and
\"4.1.3.4 Root Activity\" for more details on root Activities.

### Advanced Topics<!-- 41b17f42 -->

#### Combining Exported Attributes and Intent Filter Settings (For Activities) 

We have explained how to implement the four types of Activities in
this guidebook: Private Activities, Public Activities, Partner
Activities, and In-house Activities. The various combinations of
permitted settings for each type of exported attribute defined in the
AndroidManifest.xml file and the intent-filter elements are defined in
the table below. Please verify the compatibility of the exported
attribute and intent-filter element with the Activity you are trying
to create.

Table 4.1‑2
```eval_rst
+---------------------------+---------------------------------------------------------+
|                           | Value of exported attribute                             |
+                           +--------------------------+--------------+---------------+
|                           | true                     | false        | Not specified |
+===========================+==========================+==============+===============+
| Intent Filter defined     | Public                   | (Do not Use) | (Do not Use)  |
+---------------------------+--------------------------+--------------+---------------+
| Intent Filter Not Defined | Public, Partner,In-house | Private      | (Do not Use)  |
+---------------------------+--------------------------+--------------+---------------+

When the exported attribute of an Activity is left unspecified, the
question of whether or not the Activity is public is determined by the
presence or absence of intent filters for that Activity. [4]_ However,
in this guidebook it is forbidden to set the exported attribute to
unspecified. In general, as mentioned previously, it is best to avoid
implementations that rely on the default behavior of any given API;
moreover, in cases where explicit methods --- such as the exported
attribute --- exist for enabling important security-related settings,
it is always a good idea to make use of those methods.

.. [4] If any intent filters are defined, the Activity is public;
    otherwise it is private. For more information, see
    https://developer.android.com/guide/topics/manifest/activity-element.html#exported.
```

The reason why an undefined intent filter and an exported attribute of
false should not be used is that there is a loophole in Android\'s
behavior, and because of how Intent filters work, other application\'s
Activities can be called unexpectedly. The following two figures below
show this explanation. Figure 4.1‑4 is an example of normal behavior
in which a Private Activity (Application A) can be called by an
implicit Intent only from the same application. The Intent filter
(action = \"X\") is defined to work only inside Application A, so this
is the expected behavior.

![](media/image36.png)
```eval_rst
.. {width="4.739583333333333in" height="2.9375in"}
```

Figure 4.1‑4

Figure 4.1‑5 below shows a scenario in which the same Intent filter
(action=\"X\") is defined in Application B as well as Application A.
Application A is trying to call a Private Activity in the same
application by sending an implicit Intent, but this time a dialogue
box asking the user which application to select is displayed, and the
Public Activity B-1 in Application B called by mistake due to the user
selection. Due to this loophole, it is possible that sensitive
information can be sent to other applications or application may
receive an unexpected retuned value.

![](media/image37.png)
```eval_rst
.. {width="4.739583333333333in"
.. height="3.8020833333333335in"}
```

Figure 4.1‑5

As shown above, using Intent filters to send implicit Intents to
Private Activities may result in unexpected behavior so it is best to
avoid this setting. In addition, we have verified that this behavior
does not depend on the installation order of Application A and
Application B.

#### Validating the Requesting Application

Here we explain the technical information about how to implement a
Partner Activity. Partner applications permit that only particular
applications which are registered in a whitelist are allowed access
and all other applications are denied. Because applications other than
in-house applications also need access permission, we cannot use
signature permissions for access control.

Simply speaking, we want to validate the application trying to use the
Partner Activity by checking if it is registered in a predefined
whitelist and allow access if it is and deny access if it is not.
Application validation is done by obtaining the certificate from the
application requesting access and comparing its hash with the one in
the whitelist.

Some developers may think that it is sufficient to just compare the
package name without obtaining the certificate, however, it is easy to
spoof the package name of a legitimate application so this is not a
good method to check for authenticity. Arbitrarily assignable values
should not be used for authentication. On the other hand, because only
the application developer has the developer key for signing its
certificate, this is a better method for identification. Since the
certificate cannot be easily spoofed, unless a malicious third party
can steal the developer key, there is a very small chance that
malicious application will be trusted. While it is possible to store
the entire certificate in the whitelist, it is sufficient to only
store the SHA-256 hash value in order to minimize the file size.

There are two restrictions for using this method.

-   The requesting application has to use startActivityForResult() instead of startActivity().
-   The requesting application can only call from an Activity.

The second restriction is the restriction imposed as a result of the
first restriction, so technically there is only a single restriction.

This restriction occurs due to the restriction of
Activity.getCallingPackage() which gets the package name of the
calling application. Activity.getCallingPackage() returns the package
name of source (requesting) application only in case it is called by
startActivityForResult(), but unfortunately, when it is called by
startActivity(), it only returns null. Because of this, when using the
method explained here, the source (requesting) application needs to
use startActivityForResult() even if it does not need to obtain a
return value. In addition, startActivityForResult() can be used only
in Activity classes, so the source (requester) is limited to
Activities.

PartnerActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Activity PartnerActivity.PartnerActivity.java
   :language: java
   :encoding: shift-jis
```

PkgCertWhitelists.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCertWhitelists.java
   :language: java
   :encoding: shift-jis
```

PkgCert.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCert.java
   :language: java
   :encoding: shift-jis
```


#### Reading Intents Sent to an Activity

In Android 5.0 (API Level 21) and later, the information retrieved
with getRecentTasks() has been limited to the caller\'s own tasks and
possibly some other tasks such as home that are known to not be
sensitive. However applications, which support the versions under
Android 5.0 (API Level 21), should protect against leaking sensitive
information.

The following describes the contents of this problem occurring in
Android 5.0 and earlier version.

Intents that are sent to the task\'s root Activity are added to the
task history. A root Activity is the first Activity started in a task.
It is possible for any application to read the Intents added to the
task history by using the ActivityManager class.

Sample code for reading the task history from an application is shown
below. To browse the task history, specify the GET\_TASKS permission
in the AndroidManifest.xml file.

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Activity MaliciousActivity.app.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```

MaliciousActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Activity MaliciousActivity.MaliciousActivity.java
   :language: java
   :encoding: shift-jis
```


You can obtain specified entries of the task history by using the
getRecentTasks() function of the AcitivityManager class. Information
about each task is stored in an instance of the
ActivityManager.RecentTaskInfo class, but Intents that were sent to
the task\'s root Activity are stored in its member variable
baseIntent. Since the root Activity is the Activity which was started
when the task was created, please be sure to not fulfill the following
two conditions when calling an Activity.

-   A new task is created when the Activity is called.
-   The called Activity is the task\'s root Activity which already exists in the background or foreground.

#### Root Activity

The root Activity is the Activity which is the starting point of a
task. In other words, this is the Activity which was launched when
task was created. For example, when the default Activity is launched
by launcher, this Activity will be the root Activity. According to the
Android specifications, the contents of Intents sent to the root
Activity can be read from arbitrary applications. So, it is necessary
to take countermeasures not to send sensitive information to the root
Activity. In this guidebook, the following three rules have been made
to avoid a called Activity to become root Activity.

-   taskAffinity should not be specified.
-   launchMode should not be specified.
-   The FLAG\_ACTIVITY\_NEW\_TASK flag should not be set in an Intent sent to an Activity.

We consider the situations that an Activity can become the root
Activity below. A called Activity becoming a root Activity depends on
the following.

-   The launch mode of the called Activity
-   The task of a called Activity and its launch mode

First of all, let me explain the \"Launch mode of called Activity.\"
Launch mode of Activity can be set by writing android:launchMode in
AndroidManifest.xml. When it\'s not written, it\'s considered as
\"standard\". In addition, launch mode can be also changed by a flag
to set to Intent. Flag \"FLAG\_ACTIVITY\_NEW\_TASK\" launches Activity
by \"singleTask\" mode.

The launch modes that can be specified are as per below. I\'ll explain
about the relation with the root activity, mainly.

##### standard

Activity which is called by this mode won\'t be root, and it belongs
to the caller side task. Every time it\'s called, Instance of Activity
is to be generated.

##### singleTop

This launch mode is the same as \"standard\", except for that the
instance is not generated when launching an Activity which is
displayed in most front side of foreground task.

##### singleTask

This launch mode determines the task to which the activity would be
belonging by Affinity value. When task which is matched with
Activity\'s affinity doesn\'t exist either in background or in
foreground, a new task is generated along with Activity\'s instance.
When task exists, neither of them is to be generated. In the former
one, the launched Activity\'s Instance becomes root.

##### singleInstance

Same as \"singleTask\", but following point is different. Only root
Activity can belongs to the newly generated task. So instance of
Activity which was launched by this mode is always root activity. Now,
we need to pay attention to the case that the class name of called
Activity and the class name of Activity which is included in a task
are different although the task which has the same name of called
Activity\'s affinity already exists.

From as above, we can get to know that Activity which was launched by
\"singleTask\" or \"singleInstance\" has the possibility to become
root. In order to secure the application\'s safety, it should not be
launched by these modes.

Next, I\'ll explain about \"Task of the called Activity and its launch
mode\". Even if Activity is called by \"standard\" mode, it becomes
root Activity in some cases depends on the task state to which
Activity belongs.

For example, think about the case that called Activity\'s task has
being run already in background.

The problem here is the case that Activity Instance of the task is
launched by "singleInstance\". When the affinity of Activity which was
called by \"standard\" is same with the task, new task is to be
generated by the restriction of existing \"singleInstance\" Activity.
However, when class name of each Activity is same, task is not
generated and existing activity Instance is to be used. In any cases,
that called Activity becomes root Activity.

As per above, the conditions that root Activity is called are
complicated, for example it depends on the state of execution. So when
developing applications, it\'s better to contrive that Activity is
called by \"standard\".

As an example of that Intent which is sent to Private Activity is read
out form other application, the sample code shows the case that caller
side Activity of private Activity is launched by \"singleInstance\"
mode. In this sample code, private activity is launched by
\"standard\" mode, but this private Activity becomes root Activity of
new task due the \"singleInstance\" condition of caller side Activity.
At this moment, sensitive information that is sent to Private Activity
is recorded task history, so it can be read out from other
applications. FYI, both caller side Activity and Private Activity have
the same affinity.

AndroidManifest.xml(Not recommended)
```eval_rst
.. literalinclude:: CodeSamples/Activity SingleInstanceActivity.app.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```


Private Activity only returns the results to the received Intent.

PrivateActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Activity SingleInstanceActivity.PrivateActivity.java
   :language: java
   :encoding: shift-jis
```


In caller side of Private Activity, Private Activity is launched by
\"standard\" mode without setting flag to Intent.

PrivateUserActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Activity SingleInstanceActivity.PrivateUserActivity.java
   :language: java
   :encoding: shift-jis
```


#### Log Output When using Activities 

When using an activity, the contents of intent are output to LogCat by
ActivityManager. The following contents are to be output to LogCat, so
in this case, sensitive information should not be included here.

-   Destination Package name
-   Destination Class name
-   URI which is set by Intent\#setData()

For example, when an application sent mails, the mail address is
unfortunately outputted to LogCat if the application would specify the
mail address to URI. So, better to send by setting Extras.

When sending a mail as below, mail address is shown to the logCat.

MainActivity.java
``` java
        // URI is output to the LogCat.
        Uri uri = Uri.parse("mailto:test@gmail.com");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(intent);
```

When using Extras, mail address is no more shown to the logCat.

MainActivity.java
``` java
        // Contents which was set to Extra, is not output to the LogCat.
        Uri uri = Uri.parse("mailto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"test@gmail.com"});
        startActivity(intent);
```

However, there are cases where other applications can read the Extras
data of intent using ActivityManager\#getRecentTasks(). Please refer
to "4.1.2.2 Do Not Specify taskAffinity (Required)", "4.1.2.3 Do Not
Specify launchMode (Required)" and "4.1.2.4　Do Not Set the
FLAG\_ACTIVITY\_NEW\_TASK Flag for Intents that Start an Activity
(Required)".

#### Protecting against Fragment Injection in PreferenceActivity
```eval_rst
When a class derived from PreferenceActivity is a public Activity, a
problem known as *Fragment Injection* [5]_ may arise. To prevent this
problem from arising, it is necessary to override
PreferenceActivity.IsValidFragment() and check the validity of its
arguments to ensure that the Activity does not handle any Fragments
without intention. (For more on the safety of input data,　 see
Section　\"3.2 Handling Input Data Carefully and Securely\".)

.. [5] For more information on Fragment Injection, consult this URL: https://securityintelligence.com/new-vulnerability-android-framework-fragment-injection/
```
Below we show a sample in which IsValidFragment() has been overridden.
Note that, if the source code has been obfuscated, class names and the
results of parameter-value comparisons may change. In this case it is
necessary to pursue alternative countermeasures.

Example of an overridden isValidFragment() method

``` java
    protected boolean isValidFragment(String fragmentName) {
        // If the source code is obfuscated, we must pursue alternative strategies
        return PreferenceFragmentA.class.getName().equals(fragmentName)
                || PreferenceFragmentB.class.getName().equals(fragmentName)
                || PreferenceFragmentC.class.getName().equals(fragmentName)
                || PreferenceFragmentD.class.getName().equals(fragmentName);
    }
```

Note that if the app\'s targetSdkVersion is 19 or greater, failure to
override PreferenceActivity.isValidFragment() will result in a
security exception and the termination of the app whenever a Fragment
is inserted \[when isValidFragment() is called\], so in this case
overriding PreferenceActivity.isValidFragment() is mandatory.

#### The Autofill framework

The Autofill framework was added in Android 8.0 (API Level 26). Using
this framework allows apps to store information entered by
users---such as user names, passwords, addresses, phone numbers, and
credit cards---and subsequently to retrieve this information as
necessary to allow the app to fill in forms automatically. This is a
convenient mechanism that reduces data-entry burdens for users;
however, because it allows a given app to pass sensitive information
such as passwords and credit cards to other apps, it must be handled
with appropriate care.

##### Overview of the framework

###### 2 components
```eval_rst
In what follows, we provide an overview of the two components [6]_
registered by the Autofill framework.

-   Apps eligible for Autofill (user apps):

    -   Pass view information (text and attributes) to Autofill service;
        receive information from Autofill service as needed to
        auto-fill forms.

    -   All apps that have Activities are user apps (when in the
        foreground).

    -   It is possible for all Views of all user apps to be eligible for
        Autofill. It is also possible to explicitly specify that any
        given individual view should be ineligible for Autofill.

    -   It is also possible to restrict an app's use of Autofill to the
        Autofill service within the same package.

-   Services that provide Autofill (Autofill services):

    -   Save View information passed by an app (requires user
        permission); provide an app with information needed for
        Autofill in a View (candidate lists).

    -   The Views eligible for this information saving are determined by
        the Autofill service. (Within the Autofill framework, by
        default information on all Views contained in an Activity are
        passed to the Autofill service.)

    -   It is also possible to construct Autofill services provided by
        third parties.

    -   It is possible for several to be present within a single
        terminal with only the service selected by the user via
        Settings enabled (None is also a possible selection.)

    -   It also possible for a Service to provide a UI to validate users
        via password entry or other mechanisms to protect the security
        of the user information handled.

.. [6] The **user app** and the **Autofill service** may belong to the same package (the same APK file) or to different packages.
```
###### Procedural flowchart for the Autofill framework

Figure 4.1‑6 is a flowchart illustrating the procedural flow of
interactions among Autofill-related components during Autofill. When
triggered by events such as motion of the focus in a user app's View,
information on that View (primarily the parent-child relationships and
various attributes of the View) is passed via the Autofill framework
to the Autofill service selected within Settings**.** Based on the
data it receives, the Autofill service fetches from a database the
information (candidate lists) needed for Autofill, then returns this
to the framework. The framework displays a candidate list to the user,
and the app carries out the Autofill operation using the data selected
by the user.

![](media/image38.png)
```eval_rst
.. {width="7.266666666666667in" height="3.325in"}
```

Figure 4.1‑6: Procedural flow among components for Autofill

Next, Figure 4.1‑7 is a flowchart illustrating the procedural flow for
saving user data via Autofill. Upon a triggering event such as when
AutofillManager\#commit() is called or when an Activity is unfocused, if
any Autofilled values for the View have been modified *and* the user has
granted permission via the Save Permission dialog box displayed by the
Autofill framework, information on the View (including text) is passed
via the Autofill framework to the Autofill service selected via
Settings, and the Autofill service stores information in the database to
complete the procedural sequence.

![](media/image39.png)
```eval_rst
.. {width="7.258333333333334in" height="3.3333333333333335in"}
```

Figure 4.1‑7: Procedural flow among components for saving user data

##### Security concerns for Autofill user apps

As noted in the section "Overview of the framework" above, the
security model adopted by the Autofill framework is premised on the
assumption that the user configures the Settings to select secure
Autofill services and makes appropriate decisions regarding which data
to pass to which Autofill service when storing data.

However, if a user unwittingly selects a non-secure Autofill service,
there is a possibility that the user may permit the storage of
sensitive information that should not be passed to the Autofill
service. In what follows we discuss the damage that could result in
such a scenario.

When saving information, if the user selects an Autofill service and
grants it permission via the Save Permission dialog box, information
for all Views contained in the Activity currently displayed by the app
in use may be passed to the Autofill service. If the Autofill service
is malware, or if other security issues arise---for example, if View
information is stored by the Autofill service on an external storage
medium or on an insecure cloud service---this could create the risk
that information handled by the app might be leaked.

On the other hand, during Autofill, if the user has selected a piece
of malware as the Autofill service, values transmitted by the malware
may be entered as input. At this point, if the security of the data
input is not adequately validated by the app or by the cloud services
to which the app sends data, risks of information leakage and/or
termination of the app or the service may arise.

Note that, as discussed above in the section *"2 components",* apps
with Activities are automatically eligible for Autofill, and thus all
developers of apps with Activities must take the risks described above
into account when designing and implementing apps. In what follows we
will present countermeasures to mitigate the risks described above we
recommend that these be adopted as appropriate based on a
consideration of the countermeasures required by an app---referring to
"3.1.3 Asset Classification and Protective Countermeasures" and other
relevant resources.

##### Steps to mitigate risk: 1
```eval_rst
As discussed above, security within the Autofill framework is
ultimately guaranteed only at the user's discretion. For this reason,
the range of countermeasures available to apps is somewhat limited.
However, there is one way to mitigate the concerns described above:
Setting the importantForAutofill attribute for a view to "no" ensures
that no View information is passed to the Autofill service (i.e. the
View is made ineligible for Autofill), even if the user cannot make
appropriate selections or permissions (such as selecting a piece of
malware as the Autofill service). [7]_

.. [7] Even after taking this step, in some cases it may not be possible
    to avoid the security concerns described above---for example, if the
    user intentionally uses Autofill. Implementing the steps described
    in **"**Steps to mitigate risk: 2**"** will improve security in
    these cases.
```
The importantForAutofill attribute may be specified by any of the
following methods.

-   Set the importantForAutofill attribute in the layout XML
-   Call View\#setImportantForAutofill()

The values that may be set for this attribute are shown below. Make
sure to use values appropriate for the specified range. In particular,
note with caution that, when a value is set to "no" for a View, that
View will be ineligible for Autofill, but its children *will* remain
eligible for Autofill. The default value is "auto."

Figure 4.1‑2

<table border="yes" bordercolor="gray">
    <thead bgcolor="lightgray">
		<tr>
			<th rowspan="2">Value</th>
			<th rowspan="2">Name of constant</th>
			<th colspan="2">Eligible for Autofill?</th>
		</tr>
		<tr>
			<th width="10%">Specified View</th>
			<th width="10%">Child View</th>
		</tr>
    </thead>
	<tbody>
		<tr>
			<td>"auto"</td>
			<td>IMPORTANT_FOR_AUTOFILL_AUTO</td>
			<td>Determined by Autofill framework</td>
			<td>Determined by Autofill framework</td>
		</tr>
		<tr>
			<td>"no"</td>
			<td>IMPORTANT_FOR_AUTOFILL_NO</td>
			<td>No</td>
			<td>Yes</td>
		</tr>
		<tr>
			<td>"noExcludeDescendants"</td>
			<td>IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS</td>
			<td>No</td>
			<td>No</td>
		</tr>
		<tr>
			<td>"yes"</td>
			<td>IMPORTANT_FOR_AUTOFILL_YES</td>
			<td>Yes</td>
			<td>Yes</td>
		</tr>
		<tr>
			<td>"yesExcludeDescendants"</td>
			<td>IMPORTANT_FOR_AUTOFILL_YES_EXCLUDE_DESCENDANTS</td>
			<td>Yes</td>
			<td>No</td>
		</tr>
	</tbody>
</table>

It is also possible to use
AutofillManager\#hasEnabledAutofillServices() to restrict the use of
Autofill functionality to Autofill services within the same package.

In what follows, we show an example that all Views in an Activity are
eligible for Autofill (whether or not a View actually uses Autofill is
determined by the Autofill service) only in case that settings have
been configured to use a Autofill service within the same package. It
is also possible to call View\#setImportantForAutofill() for
individual Views.

DisableForOtherServiceActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Autofill Sample.DisableForOtherServiceActivity.java
   :language: java
   :encoding: shift-jis
```

##### Steps to mitigate risk: 2

Even in cases where an app has implemented the steps described in the
previous section ("Steps to mitigate risk: 1"), the user can forcibly
enable the use of Autofill by long-pressing the View, displaying the
floating toolbar or a similar control interface, and selecting
"Automatic input." In this case, information for all Views---including
Views for which the importantForAutofill attribute has been set to
"no," or for which similar steps have been taken---will be passed to
the Autofill service.

It is possible to avoid the risk of information leakage even in
circumstances such as these by deleting the "Automatic Input" option
from the floating-toolbar menu and other control interfaces; this step
is to be carried out in addition to the procedures described in "Steps
to mitigate risk: 1"

Sample code for this purpose is shown below.

DisableAutofillActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Autofill Sample.DisableAutofillActivity.java
   :language: java
   :encoding: shift-jis
```

Receiving/Sending Broadcasts
----------------------------

### Sample Code<!-- a4bc3cfd -->

Creating Broadcast Receiver is required to receive Broadcast. Risks
and countermeasures of using Broadcast Receiver differ depending on
the type of the received Broadcast.

You can find your Broadcast Receiver in the following judgment flow.
The receiving applications cannot check the package names of
Broadcast-sending applications that are necessary for linking with the
partners. As a result, Broadcast Receiver for the partners cannot be
created.

Table 4.2‑1 Definition of broadcast receiver types
```eval_rst
=========================== ===================================
Type                        | Definition
=========================== ===================================
Private broadcast receiver  | A broadcast receiver that can receive broadcasts only from
                            | the same application, therefore is the safest broadcast
                            | receiver
Public broadcast receiver   | A broadcast receiver that can receive broadcasts from an
                            | unspecified large number of applications
                            |
                            | If the app's targetSDKVersion is 26 or above, then, on
                            | terminals running Android 8.0 (API level 26） or later,
                            | Broadcast Receivers may not be registered for implicit
                            | Broadcast Intents [8]_
In-house broadcast receiver | A broadcast receiver that can receive broadcasts only from
                            | other In-house applications
=========================== ===================================

.. [8] As exceptions to this rule, some implicit Broadcast Intents sent by the system may use Broadcast Receivers. For more information, consult the following URL.
   https://developer.android.com/guide/components/broadcast-exceptions.html
```

![](media/image40.png)
```eval_rst
.. {width="6.395833333333333in"
.. height="3.4895833333333335in"}
```

Figure 4.2‑1

In addition, Broadcast Receiver can be divided into 2 types based on
the definition methods, Static Broadcast Receiver and Dynamic
Broadcast Receiver. The differences between them can be found in the
following figure. In the sample code, an implementation method for
each type is shown. The implementation method for sending applications
is also described because the countermeasure for sending information
is determined depending on the receivers.

Table 4.2‑2
```eval_rst
========================== ======================== =======================================
..                         | Definition method      | Characteristic
========================== ======================== =======================================
Static Broadcast Receiver  | Define by writing      | - There is a restriction that some Broadcasts
                           | <receiver> elements    | (e.g. ACTION_BATTERY_CHANGED) sent by
                           | in AndroidManifest.xml | system cannot be received.
                                                    |
                                                    | - Broadcast can be received from application's
                                                    | initial boot till uninstallation.
Dynamic Broadcast Receiver | By calling             | - Broadcasts which cannot be received by
                           | registerReceiver()     | static Broadcast Receiver can be received.
                           | and                    |
                           | unregisterReceiver()   | - The period of receiving Broadcasts can be
                           | in a program,          | controlled by the program. For example,
                           | register/unregister    | Broadcasts can be received only while
                           | Broadcast Receiver     | Activity is on the front side.
                           | dynamically.           |
                                                    | - Private Broadcast Receiver cannot be created.
========================== ======================== =======================================

```

#### Private Broadcast Receiver - Receiving/Sending Broadcasts

Private Broadcast Receiver is the safest Broadcast Receiver because only Broadcasts sent from within the application can be received. Dynamic Broadcast Receiver cannot be registered as Private, so Private Broadcast Receiver consists of only Static Broadcast Receivers.

Points (Receiving Broadcasts):

1. Explicitly set the exported attribute to false.
2. Handle the received intent carefully and securely, even though the intent was sent from within the same application.
3. Sensitive information can be sent as the returned results since the requests come from within the same application.

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Broadcast PrivateReceiver.app.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```

PrivateReceiver.java
```eval_rst
.. literalinclude:: CodeSamples/Broadcast PrivateReceiver.PrivateReceiver.java
   :language: java
   :encoding: shift-jis
```

The sample code for sending Broadcasts to private Broadcast Receiver is shown below.

Points (Sending Broadcasts):

4. Use the explicit Intent with class specified to call a receiver within the same application.
5. Sensitive information can be sent since the destination Receiver is within the same application.
6. Handle the received result data carefully and securely, even though the data came from the Receiver within the same application.

PrivateSenderActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Broadcast PrivateReceiver.PrivateSenderActivity.java
   :language: java
   :encoding: shift-jis
```

#### Public Broadcast Receiver - Receiving/Sending Broadcasts

Public Broadcast Receiver is the Broadcast Receiver that can receive Broadcasts from unspecified large number of applications, so it\'s necessary to pay attention that it may receive Broadcasts from malware.

Points (Receiving Broadcasts):

1. Explicitly set the exported attribute to true.
2. Handle the received Intent carefully and securely.
3. When returning a result, do not include sensitive information.

Public Receiver which is the sample code for public Broadcast Receiver can be used both in static Broadcast Receiver and Dynamic Broadcast Receiver.

PublicReceiver.java
```eval_rst
.. literalinclude:: CodeSamples/Broadcast PublicReceiver.PublicReceiver.java
   :language: java
   :encoding: shift-jis
```

Static Broadcast Receive is defined in AndroidManifest.xml. Note with caution that---depending on the terminal version---reception of implicit Broadcast Intents may be restricted, as in 「Table 4.2‑1」.

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Broadcast PublicReceiver.app.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```

In Dynamic Broadcast Receiver, registration/unregistration is executed by calling registerReceiver() or unregisterReceiver() in the program. In order to execute registration/unregistration by button operations, the button is allocated on PublicReceiverActivity. Since the scope of Dynamic Broadcast Receiver Instance is longer than PublicReceiverActivity, it cannot be kept as the member variable of PublicReceiverActivity. In this case, keep the Dynamic Broadcast Receiver Instance as the member variable of DynamicReceiverService, and then start/end DynamicReceiverService from PublicReceiverActivity to register/unregister Dynamic Broadcast Receiver indirectly.

DynamicReceiverService.java
```eval_rst
.. literalinclude:: CodeSamples/Broadcast PublicReceiver.DynamicReceiverService.java
   :language: java
   :encoding: shift-jis
```

PublicReceiverActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Broadcast PublicReceiver.PublicReceiverActivity.java
   :language: java
   :encoding: shift-jis
```

Next, the sample code for sending Broadcasts to public Broadcast Receiver is shown. When sending Broadcasts to public Broadcast Receiver, it\'s necessary to pay attention that Broadcasts can be received by malware.

Points (Sending Broadcasts):

4. Do not send sensitive information.
5. When receiving a result, handle the result data carefully and securely.

PublicSenderActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Broadcast PublicSender.PublicSenderActivity.java
   :language: java
   :encoding: shift-jis
```

#### In-house Broadcast Receiver - Receiving/Sending Broadcasts

In-house Broadcast Receiver is the Broadcast Receiver that will never receive any Broadcasts sent from other than in-house applications. It consists of several in-house applications, and it\'s used to protect the information or functions that in-house application handles.

Points (Receiving Broadcasts):

1. Define an in-house signature permission to receive Broadcasts.
2. Declare to use the in-house signature permission to receive results.
3. Explicitly set the exported attribute to true.
4. Require the in-house signature permission by the Static Broadcast Receiver definition.
5. Require the in-house signature permission to register Dynamic Broadcast Receiver.
6. Verify that the in-house signature permission is defined by an in-house application.
7. Handle the received intent carefully and securely, even though the Broadcast was sent from an in-house application.
8. Sensitive information can be returned since the requesting application is in-house.
9. When Exporting an APK, sign the APK with the same developer key as the sending application.

In-house Receiver which is a sample code of in-house Broadcast Receiver is to be used both in Static Broadcast Receiver and Dynamic Broadcast Receiver.

InhouseReceiver.java
```eval_rst
.. literalinclude:: CodeSamples/Broadcast InhouseReceiver.InhouseReceiver.java
   :language: java
   :encoding: shift-jis
```

Static Broadcast Receiver is to be defined in AndroidManifest.xml.Note with caution that---depending on the terminal version---reception of implicit Broadcast Intents may be restricted, as in 「Table 4.2‑1」.

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Broadcast InhouseReceiver.broadcastInhouseReceiver.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```

Dynamic Broadcast Receiver executes registration/unregistration by calling registerReceiver() or unregisterReceiver() in the program. In order to execute registration/unregistration by the button operations, the button is arranged on InhouseReceiverActivity. Since the scope of Dynamic Broadcast Receiver Instance is longer than InhouseReceiverActivity, it cannot be kept as the member variable of InhouseReceiverActivity. So, keep Dynamic Broadcast Receiver Instance as the member variable of DynamicReceiverService, and then start/end DynamicReceiverService from InhouseReceiverActivity to register/unregister Dynamic Broadcast Receiver indirectly.

InhouseReceiverActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Broadcast InhouseReceiver.InhouseReceiverActivity.java
   :language: java
   :encoding: shift-jis
```

DynamicReceiverService.java
```eval_rst
.. literalinclude:: CodeSamples/Broadcast InhouseReceiver.DynamicReceiverService.java
   :language: java
   :encoding: shift-jis
```

SigPerm.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.SigPerm.java
   :language: java
   :encoding: shift-jis
```

PkgCert.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCert.java
   :language: java
   :encoding: shift-jis
```

\*\*\* Point 9 \*\*\* When exporting an APK, sign the APK with the same developer key as the sending application.

![](media/image35.png)
```eval_rst
.. {width="4.647222222222222in"
.. height="3.2743055555555554in"}
```

Figure 4.2‑2

Next, the sample code for sending Broadcasts to in-house Broadcast Receiver is shown.

Points (Sending Broadcasts):

10. Define an in-house signature permission to receive results.
11. Declare to use the in-house signature permission to receive Broadcasts.
12. Verify that the in-house signature permission is defined by an in-house application.
13. Sensitive information can be returned since the requesting application is the in-house one.
14. Require the in-house signature permission of Receivers.
15. Handle the received result data carefully and securely.
16. When exporting an APK, sign the APK with the same developer key as the destination application.

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Broadcast InhouseSender.broadcastInhouseSender.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```

InhouseSenderActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Broadcast InhouseSender.InhouseSenderActivity.java
   :language: java
   :encoding: shift-jis
```

SigPerm.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.SigPerm.java
   :language: java
   :encoding: shift-jis
```

PkgCert.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCert.java
   :language: java
   :encoding: shift-jis
```

\*\*\* Point 16 \*\*\* When exporting an APK, sign the APK with the same developer key as the destination application.

![](media/image35.png)
```eval_rst
.. {width="4.647222222222222in"
.. height="3.2743055555555554in"}
```

Figure 4.2‑3

### Rule Book<!-- 11babdbd -->

Follow the rules below to Send or receive Broadcasts.

1. Broadcast Receiver that Is Used Only in an Application Must Be Set as Private (Required)
2.  Handle the Received Intent Carefully and Securely (Required)
3.  Use the In-house Defined Signature Permission after Verifying that it\'s Defined by an In-house Application (Required)
4.  When Returning a Result Information, Pay Attention to the Result Information Leakage from the Destination Application (Required)
5.  When Sending Sensitive Information with a Broadcast, Limit the Receivable Receiver (Required)
6.  Sensitive Information Must Not Be Included in the Sticky Broadcast (Required)
7.  Pay Attention that the Ordered Broadcast without Specifying the receiverPermission May Not Be Delivered (Required)
8.  Handle the Returned Result Data from the Broadcast Receiver Carefully and Securely (Required)
9.  When Providing an Asset Secondarily, the Asset should be protected with the Same Protection Level (Required)

#### Broadcast Receiver that Is Used Only in an Application Must Be Set as Private (Required)

Broadcast Receiver which is used only in the application should be set as private to avoid from receiving any Broadcasts from other applications unexpectedly. It will prevent the application function abuse or the abnormal behaviors.

Receiver used only within the same application should not be designed with setting Intent-filter. Because of the Intent-filter characteristics, a public Receiver of other application may be called unexpectedly by calling through Intent-filter even though a private Receiver within the same application is to be called.

AndroidManifest.xml(Not recommended)
``` xml
        <!-- Private Broadcast Receiver -->
        <!-- *** POINT 1 *** Set the exported attribute to false explicitly. -->
        <receiver android:name=".PrivateReceiver"
            android:exported="false" >
            <intent-filter>
                <action android:name="org.jssec.android.broadcast.MY_ACTION" />
            </intent-filter>
        </receiver>
```

Please refer to \"4.2.3.1 Combinations of the exported Attribute and the Intent-filter setting (For Receiver).\"

#### Handle the Received Intent Carefully and Securely (Required)

Though risks are different depending on the types of the Broadcast Receiver, firstly verify the safety of Intent when processing received Intent data.

Since Public Broadcast Receiver receives the Intents from unspecified large number of applications, it may receive malware\'s attacking Intents. Private Broadcast Receiver will never receive any Intent from other applications directly, but Intent data which a public Component received from other applications may be forwarded to Private Broadcast Receiver. So don\'t think that the received Intent is totally safe without any qualification. In-house Broadcast Receivers have some degree of the risks, so it also needs to verify the safety of the received Intents.

Please refer to \"3.2 Handling Input Data Carefully and Securely\"

#### Use the In-house Defined Signature Permission after Verifying that it\'s Defined by an In-house Application (Required)

In-house Broadcast Receiver which receives only Broadcasts sent by an In-house application should be protected by in-house-defined Signature Permission. Permission definition/Permission request declarations in AndroidManifest.xml are not enough to protecting, so please refer to \"5.2.1.2 How to Communicate Between In-house Applications with In-house-defined Signature Permission.\" ending Broadcasts by specifying in-house-defined Signature Permission to receiverPermission parameter requires verification in the same way.

#### When Returning a Result Information, Pay Attention to the Result Information Leakage from the Destination Application (Required)

The Reliability of the application which returns result information by setResult() varies depending on the types of the Broadcast Receiver. In case of Public Broadcast Receiver, the destination application may be malware, and there may be a risk that the result information is used maliciously. In case of Private Broadcast Receiver and In-house Broadcast Receiver, the result destination is In-house developed application, so no need to mind the result information handling.

Need to pay attention to the result information leakage from the destination application when result information is returned from Broadcast Receivers as above.

#### When Sending Sensitive Information with a Broadcast, Limit the Receivable Receiver (Required)

Broadcast is the created system to broadcast information to unspecified large number of applications or notify them of the timing at once. So, broadcasting sensitive information requires the careful designing for preventing the illicit obtainment of the information by malware.

For broadcasting sensitive information, only reliable Broadcast Receiver can receive it, and other Broadcast Receivers cannot. The following are some examples of Broadcast sending methods.

- The method is to fix the address by Broadcast-sending with an
  explicit Intent for sending Broadcasts to the intended reliable
  Broadcast Receivers only. There are 2 patterns in this method.

  - When it\'s addressed to a Broadcast Receiver within the same
    application, specify the address by Intent\#setClass(Context,
    Class). Refer to sample code section \"4.2.1.1 Private Broadcast
    Receiver - Receiving/Sending Broadcast\" for the concrete code.

  - When it\'s addressed to a Broadcast Receiver in other
    applications, specify the address by
    Intent\#setClassName(String, String). Confirm the permitted
    application by comparing the developer key of the APK signature
    in the destination package with the white list to send
    Broadcasts. Actually the following method of using implicit
    Intents is more practical.

- The Method is to send Broadcasts by specifying in-house-defined
  Signature Permission to receiverPermission parameter and make the
  reliable Broadcast Receiver declare to use this Signature
  Permission. Refer to the sample code section \"4.2.1.3 In-house
  Broadcast Receiver - Receiving/Sending Broadcast\" for the concrete
  code. In addition, implementing this Broadcast-sending method needs
  to apply the rule \"4.2.2.3 Use the In-house Defined Signature
  Permission after Verifying that it\'s Defined by an In-house
  Application (Required).\"

#### Sensitive Information Must Not Be Included in the Sticky Broadcast (Required)

Usually, the Broadcasts will be disappeared when they are processed to be received by the available Broadcast Receivers. On the other hand, Sticky Broadcasts (hereafter, Sticky Broadcasts including Sticky Ordered Broadcasts), will not be disappeared from the system even when they processed to be received by the available Broadcast Receivers and will be able to be received by registerReceiver(). When Sticky Broadcast becomes unnecessary, it can be deleted anytime arbitrarily with removeStickyBroadcast().

As it\'s presupposed that Sticky Broadcast is used by the implicit
Intent. Broadcasts with specified receiverPermission Parameter cannot
be sent. For this reason, information sent via Sticky Broadcasts can
be accessed by multiple unspecified apps --- including malware --- and
thus sensitive information must not be sent in this way. Note that
Sticky Broadcast is deprecated in Android 5.0 (API Level 21).

####  Pay Attention that the Ordered Broadcast without Specifying the receiverPermission May Not Be Delivered (Required)

Ordered Broadcast without specified receiverPermission Parameter can
be received by unspecified large number of applications including
malware. Ordered Broadcast is used to receive the returned information
from Receiver, and to make several Receivers execute processing one by
one. Broadcasts are sent to the Receivers in order of priority. So if
the high- priority malware receives Broadcast first and executes
abortBroadcast(), Broadcasts won\'t be delivered to the following
Receivers.

#### Handle the Returned Result Data from the Broadcast Receiver Carefully and Securely (Required)

Basically the result data should be processed safely considering the
possibility that received results may be the attacking data though the
risks vary depending on the types of the Broadcast Receiver which has
returned the result data.

When sender (source) Broadcast Receiver is public Broadcast Receiver,
it receives the returned data from unspecified large number of
applications. So it may also receive malware\'s attacking data. When
sender (source) Broadcast Receiver is private Broadcast Receiver, it
seems no risk. However the data received by other applications may be
forwarded as result data indirectly. So the result data should not be
considered as safe without any qualification. When sender (source)
Broadcast Receiver is In-house Broadcast Receiver, it has some degree
of the risks. So it should be processed in a safe way considering the
possibility that the result data may be an attacking data.

Please refer to \"3.2 Handling Input Data Carefully and Securely\"

####  When Providing an Asset Secondarily, the Asset should be protected with the Same Protection Level (Required)

When information or function assets protected by Permission are
provided to other applications secondarily, it\'s necessary to keep
the protection standard by claiming the same Permission of the
destination application. In the Android Permission security models,
privileges are managed only for the direct access to the protected
assets from applications. Because of the characteristics, acquired
assets may be provided to other applications without claiming
Permission which is necessary for protection. This is actually same as
re-delegating Permission, as it is called, Permission re-delegation
problem. Please refer to \"5.2.3.4 Permission Re-delegation Problem.\"

### Advanced Topics<!-- acfc87e9 -->

#### Combinations of the exported Attribute and the Intent-filter setting (For Receiver)

Table 4.2-3 represents the permitted combination of export settings
and Intent-filter elements when implementing Receivers. The reason why
the usage of exported=\"false\" with Intent-filter definition is
principally prohibited, is described below.

Table 4.2‑3 Usable or not; Combination of exported attribute and intent-filter elements
```eval_rst
+---------------------------+-------------------------------------+
|                           | Value of exported attribute         |
+                           +------+--------------+---------------+
|                           | True | False        | Not specified |
+===========================+======+==============+===============+
| Intent-filter defined     | OK   | (Do not Use) | (Do not Use)  |
+---------------------------+------+--------------+---------------+
| Intent Filter Not Defined | OK   | OK           | (Do not Use)  |
+---------------------------+------+--------------+---------------+

When the exported attribute of a Receiver is left unspecified, the
question of whether or not the Receiver is public is determined by the
presence or absence of intent filters for that Receiver. [9]_ However,
in this guidebook it is forbidden to set the exported attribute to
unspecified. In general, as mentioned previously, it is best to avoid
implementations that rely on the default behavior of any given API;
moreover, in cases where explicit methods --- such as the exported
attribute --- exist for enabling important security-related settings,
it is always a good idea to make use of those methods.

.. [9] If any intent filters are defined then the Receiver is public; otherwise it is private. For more information, see https://developer.android.com/guide/topics/manifest/receiver-element.html#exported.
```

Public Receivers in other applications may be called unexpectedly even
though Broadcasts are sent to the private Receivers within the same
applications. This is the reason why specifying exported=\"false\"
with Intent-filter definition is prohibited. The following 2 figures
show how the unexpected calls occur.

Figure 4.2‑4 is an example of the normal behaviors which a private
Receiver (application A) can be called by implicit Intent only within
the same application. Intent-filter (in the figure, action=\"X\") is
defined only in application A, so this is the expected behavior.

![](media/image41.png)
```eval_rst
.. {width="4.739583333333333in"
.. height="3.8020833333333335in"}
```

Figure 4.2‑4

Figure 4.2‑5 is an example that Intent-filter (see action=\"X\" in the
figure) is defined in the application B as well as in the application
A. First of all, when another application (application C) sends
Broadcasts by implicit Intent, they are not received by a private
Receiver (A-1) side. So there won\'t be any security problem. (See the
orange arrow marks in the Figure.)

From security point of view, the problem is application A\'s call to
the private Receiver within the same application. When the application
A broadcasts implicit Intent, not only Private Receiver within the
same application, but also public Receiver (B-1) with the same
Intent-filter definition can also receive the Intent. (Red arrow marks
in the Figure). In this case, sensitive information may be sent from
the application A to B. When the application B is malware, it will
cause the leakage of sensitive information. When the Broadcast is
Ordered Broadcast, it may receive the unexpected result information.

![](media/image42.png)
```eval_rst
.. {width="4.739583333333333in"
.. height="3.8020833333333335in"}
```

Figure 4.2‑5

However, exported=\"false\" with Intent-filter definition should be
used when Broadcast Receiver to receive only Broadcast Intent sent by
the system is implemented. Other combination should not be used. This
is based on the fact that Broadcast Intent sent by the system can be
received by exported=\"false\". If other applications send Intent
which has same ACTION with Broadcast Intent sent by system, it may
cause an unexpected behavior by receiving it. However, this can be
prevented by specifying exported=\"false\".

#### Receiver Won\'t Be Registered before Launching the Application

```eval_rst
It is important to note carefully that a Broadcast Receiver defined
statically in AndroidManifest.xml will not be automatically enabled
upon installation. [10]_ Apps are able to receive Broadcasts only after
they have been launched the first time; thus, it is not possible to
use the receipt of a Broadcast after installation as a trigger to
initiate operations. However, if the
Intent.FLAG\_INCLUDE\_STOPPED\_PACKAGES flag set when sending a
Broadcast, that Broadcast will be received even by apps that have not
yet been launched for the first time.

.. [10] In versions prior to Android 3.0, Receivers were registered automatically simply by installing apps.
```

#### Private Broadcast Receiver Can Receive the Broadcast that Was Sent by the Same UID Application

Same UID can be provided to several applications. Even if it\'s
private Broadcast Receiver, the Broadcasts sent from the same UID
application can be received.

However, it won\'t be a security problem. Since it\'s guaranteed that
applications with the same UID have the consistent developer keys for
signing APK. It means that what private Broadcast Receiver receives is
only the Broadcast sent from In-house applications.

#### Types and Features of Broadcasts

Regarding Broadcasts, there are 4 types based on the combination of
whether it\'s Ordered or not, and Sticky or not. Based on Broadcast
sending methods, a type of Broadcast to send is determined. Note that
Sticky Broadcast is deprecated in Android 5.0 (API Level 21).

Table 4.2‑4

```eval_rst
========================== ============================== ========== =========
Type of Broadcast          Method for sending             Ordered?   Sticky?
========================== ============================== ========== =========
Normal Broadcast           sendBroadcast()                No         No
Ordered Broadcast          sendOrderedBroadcast()         Yes        No
Sticky Broadcast           sendStickyBroadcast()          No         Yes
Sticky Ordered Broadcast   sendStickyOrderedBroadcast()   Yes        Yes
========================== ============================== ========== =========
```

The feature of each Broad cast is described.

Table 4.2‑5

```eval_rst
========================= ===========================================================
Type of Broadcast         | Features for each type of Broadcast
========================= ===========================================================
Normal Broadcast          | Normal Broadcast disappears when it is sent to receivable Broadcast Receiver.
                          | Broadcasts are received by several Broadcast Receivers simultaneously.
                          | This is a difference from Ordered Broadcast.
                          | Broadcasts are allowed to be received by the particular Broadcast Receivers.
Ordered Broadcast         | Ordered Broadcast is characterized by receiving Broadcasts one by one in order
                          | with receivable Broadcast Receivers.
                          | The higher-priority Broadcast Receiver receives earlier.
                          | Broadcasts will disappear when Broadcasts are delivered to all Broadcast Receivers or
                          | a Broadcast Receiver in the process calls abortBroadcast().
                          | Broadcasts are allowed to be received by the Broadcast Receivers which declare
                          | the specified Permission.
                          | In addition, the result information sent from Broadcast Receiver can be received
                          | by the sender with Ordered Broadcasts.
                          | The Broadcast of SMS-receiving notice (SMS\_RECEIVED) is a representative example
                          | of Ordered Broadcast.
Sticky Broadcast          | Sticky Broadcast does not disappear and remains in the system, and then
                          | the application that calls registerReceiver() can receive Sticky Broadcast later.
                          | Since Sticky Broadcast is different from other Broadcasts, it will never disappear
                          | automatically. So when Sticky Broadcast is not necessary, calling
                          | removeStickyBroadcast() explicitly is required to delete Sticky Broadcast.
                          | Also, Broadcasts cannot be received by the limited Broadcast Receivers with
                          | particular Permission. The Broadcast of changing battery-state notice
                          | (ACTION\_BATTERY\_CHANGED) is the representative example of Sticky Broadcast.
Sticky Ordered Broadcast  | This is the Broadcast which has both characteristics of Ordered Broadcast and
                          | Sticky Broadcast. Same as Sticky Broadcast, it cannot allow only Broadcast Receivers
                          | with the particular Permission to receive the Broadcast.
========================= ===========================================================
```

From the Broadcast characteristic behavior point of view, above table
is conversely arranged in the following one.

Table 4.2‑6

```eval_rst
+----------------------+------------+------------+------------+------------+
|| Characteristic      || Normal    || Ordered   || Sticky    || Sticky    |
|| behavior of         ||           ||           ||           || Ordered   |
|| Broadcast           || Broadcast || Broadcast || Broadcast || Broadcast |
+======================+============+============+============+============+
|| Limit Broadcast     || OK        || OK        || \-        || \-        |
|| Receivers which can |            |            |            |            |
|| receive Broadcast,  |            |            |            |            |
|| by Permission       |            |            |            |            |
+----------------------+------------+------------+------------+------------+
|| Get the results     || \-        || OK        || \-        || OK        |
|| of process from     |            |            |            |            |
|| Broadcast Receiver  |            |            |            |            |
+----------------------+------------+------------+------------+------------+
|| Make Broadcast      || \-        || OK        || \-        || OK        |
|| Receivers process   |            |            |            |            |
|| Broadcasts in order |            |            |            |            |
+----------------------+------------+------------+------------+------------+
|| Receive Broadcasts  || \-        || \-        || OK        || OK        |
|| later, which have   |            |            |            |            |
|| been already sent   |            |            |            |            |
+----------------------+------------+------------+------------+------------+
```

#### Broadcasted Information May be Output to the LogCat

Basically sending/receiving Broadcasts is not output to LogCat.
However, the error log will be output when lacking Permission causes
errors in receiver/sender side. Intent information sent by Broadcast
is included in the error log, so after an error occurs it\'s necessary
to pay attention that Intent information is displayed in LogCat when
Broadcast is sent.

Erorr of lacking Permission in sender side

```
W/ActivityManager(266): Permission Denial: broadcasting Intent {
act=org.jssec.android.broadcastreceiver.creating.action.MY_ACTION }
from org.jssec.android.broadcast.sending (pid=4685, uid=10058) requires
org.jssec.android.permission.MY_PERMISSION due to receiver
org.jssec.android.broadcastreceiver.creating/org.jssec.android.broadcastreceiver.creating.CreatingType3Receiver
```

Erorr of lacking Permission in receiver side

```
W/ActivityManager(275): Permission Denial: receiving Intent {
act=org.jssec.android.broadcastreceiver.creating.action.MY_ACTION } to
org.jssec.android.broadcastreceiver.creating requires
org.jssec.android.permission.MY_PERMISSION due to sender
org.jssec.android.broadcast.sending (uid 10158)
```

#### Items to Keep in Mind When Placing an App Shortcut on the Home Screen

In what follows we discuss a number of items to keep in mind when
creating a shortcut button for launching an app from the home screen
or for creating URL shortcuts such as bookmarks in web browsers. As an
example, we consider the implementation shown below.

Place an app shortcut on the home screen

``` java
        Intent targetIntent = new Intent(this, TargetActivity.class);

        // Intent to request shortcut creation
        Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        // Specify an Intent to be launched when the shortcut is tapped
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, targetIntent);
        Parcelable icon = Intent.ShortcutIconResource.fromContext(context, iconResource);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        intent.putExtra("duplicate", false);

        // Use Broadcast to send the system our request for shortcut creation
        context.sendBroadcast(intent);
```

In the Broadcast sent by the above code snippet, the receiver is the
home-screen app, and it is difficult to identify the package name; one
must take care to remember that this is a transmission to a public
receiver with an implicit intent. Thus the Broadcast sent by this
snippet could be received by any arbitrary app, including malware; for
this reason, the inclusion of sensitive information in the Intent may
create the risk of a damaging leak of information. It is particularly
important to note that, when creating a URL-based shortcut, secret
information may be contained in the URL itself.

As countermeasures, it is necessary to follow the points listed in
"4.2.1.2 Public Broadcast Receiver - Receiving/Sending Broadcasts" and
to ensure that the transmitted Intent does not contain sensitive
information.

Creating/Using Content Providers
--------------------------------

Since the interface of ContentResolver and SQLiteDatabase are so much
alike, it\'s often misunderstood that Content Provider is so closely
related to SQLiteDatabase. However, actually Content Provider simply
provides the interface of inter-application data sharing, so it\'s
necessary to pay attention that it does not interfere each data saving
format. To save data in Content Provider, SQLiteDatabase can be used,
and other saving formats, such as an XML file format, also can be
used. Any data saving process is not included in the following sample
code, so please add it if needed.

### Sample Code<!-- 45c21b70 -->

The risks and countermeasures of using Content Provider differ
depending on how that Content Provider is being used. In this section,
we have classified 5 types of Content Provider based on how the
Content Provider is being used. You can find out which type of Content
Provider you are supposed to create through the following chart shown below.

Table 4.3‑1 Definition of content provider types

```eval_rst
=========================== ===================================
| Type                      | Definition
=========================== ===================================
| Private Content Provider  | A content provider that cannot be used by another application,
                            | and therefore is the safest content provider
| Public Content Provider   | A content provider that is supposed to be used by an unspecified
                            | large number of applications
| Partner Content Provider  | A content provider that can be used by specific applications
                            | made by a trusted partner company.
| In-house Content Provider | A content provider that can only be used by other in-house applications
| Temporary permit          | A content provider that is basically private content provider,
| Content Provider          | but permits specific applications to access the particular URI.
=========================== ===================================
```

![](media/image43.png)
```eval_rst
.. {width="6.889763779527559in"
.. height="3.0866141732283463in"}
```

Figure 4.3‑1

#### Creating/Using Private Content Providers

```eval_rst
Private Content Provider is the Content Provider which is used only in
the single application, and the safest Content Provider [11]_.

.. [11] However, non-public settings for Content Provider are not functional in Android 2.2 (API Level 8) and previous versions.
```

Sample code of how to implement a private Content Provider is shown below.

Points (Creating a Content Provider):

1.  Explicitly set the exported attribute to false.
2.  Handle the received request data carefully and securely, even though
    the data comes from the same application.
3.  Sensitive information can be sent since it is sending and receiving
    all within the same application.

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Provider PrivateProvider.app.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```

PrivateProvider.java
```eval_rst
.. literalinclude:: CodeSamples/Provider PrivateProvider.PrivateProvider.java
   :language: java
   :encoding: shift-jis
```

Next is an example of Activity which uses Private Content Provider.

Points (Using a Content Provider):

4. Sensitive information can be sent since the destination provider is
   in the same application.

5. Handle received result data carefully and securely, even though the
   data comes from the same application.

PrivateUserActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Provider PrivateProvider.PrivateUserActivity.java
   :language: java
   :encoding: shift-jis
```

#### Creating/Using Public Content Providers

Public Content Provider is the Content Provider which is supposed to
be used by unspecified large number of applications. It\'s necessary
to pay attention that since this doesn\'t specify clients, it may be
attacked and tampered by Malware. For example, a saved data may be
taken by select(), a data may be changed by update(), or a fake data
may be inserted/deleted by insert()/delete().

In addition, when using a custom Public Content Provider which is not
provided by Android OS, it\'s necessary to pay attention that request
parameter may be received by Malware which masquerades as the custom
Public Content Provider, and also the attack result data may be sent.
Contacts and MediaStore provided by Android OS are also Public Content
Providers, but Malware cannot masquerades as them.

Sample code to implement a Public Content Provider is shown below.

Points (Creating a Content Provider):

1.  Explicitly set the exported attribute to true.
2.  Handle the received request data carefully and securely.
3.  When returning a result, do not include sensitive information.

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Provider PublicProvider.app.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```

PublicProvider.java
```eval_rst
.. literalinclude:: CodeSamples/Provider PublicProvider.PublicProvider.java
   :language: java
   :encoding: shift-jis
```

Next is an example of Activity which uses Public Content Provider.

Points (Using a Content Provider):

4. Do not send sensitive information.

5. When receiving a result, handle the result data carefully and securely.

PublicUserActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Provider PublicUser.PublicUserActivity.java
   :language: java
   :encoding: shift-jis
```

#### Creating/Using Partner Content Providers

Partner Content Provider is the Content Provider which can be used
only by the particular applications. The system consists of a partner
company\'s application and In-house application, and it is used to
protect the information and features which are handled between a
partner application and an In-house application.

Sample code to implement a partner-only Content Provider is shown below.

Points (Creating a Content Provider):

1. Explicitly set the exported attribute to true.
2. Verify if the certificate of a requesting application has been
   registered in the own white list.
3. Handle the received request data carefully and securely, even though
   the data comes from a partner application.
4. Information that is granted to disclose to partner applications can
   be returned.

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Provider PartnerProvider.providerPartnerProvider.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```

PartnerProvider.java
```eval_rst
.. literalinclude:: CodeSamples/Provider PartnerProvider.PartnerProvider.java
   :language: java
   :encoding: shift-jis
```

Next is an example of Activity which use partner only Content Provider.

Points (Using a Content Provider):

5. Verify if the certificate of the target application has been
   registered in the own white list.

6. Information that is granted to disclose to partner applications can
   be sent.

7. Handle the received result data carefully and securely, even though
   the data comes from a partner application.

PartnerUserActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Provider PartnerUser.PartnerUserActivity.java
   :language: java
   :encoding: shift-jis
```

PkgCertWhitelists.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCertWhitelists.java
   :language: java
   :encoding: shift-jis
```

PkgCert.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCert.java
   :language: java
   :encoding: shift-jis
```

#### Creating/Using In-house Content Providers

In-house Content Provider is the Content Provider which prohibits to
be used by applications other than In house only applications.

Sample code of how to implement an In house only Content Provider is shown below.

Points (Creating a Content Provider):

1.  Define an in-house signature permission.
2.  Require the in-house signature permission.
3.  Explicitly set the exported attribute to true.
4.  Verify if the in-house signature permission is defined by an
    in-house application.
5.  Verify the safety of the parameter even if it\'s a request from In
    house only application.
6.  Sensitive information can be returned since the requesting
    application is in-house.
7.  When exporting an APK, sign the APK with the same developer key as
    that of the requesting application.

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Provider InhouseProvider.providerInhouseProvider.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```

InhouseProvider.java
```eval_rst
.. literalinclude:: CodeSamples/Provider InhouseProvider.InhouseProvider.java
   :language: java
   :encoding: shift-jis
```

SigPerm.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.SigPerm.java
   :language: java
   :encoding: shift-jis
```

PkgCert.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCert.java
   :language: java
   :encoding: shift-jis
```

\*\*\* Point 7 \*\*\* When exporting an APK, sign the APK with the
same developer key as the requesting application.

![](media/image35.png)
```eval_rst
.. {width="4.647222222222222in"
.. height="3.2743055555555554in"}
```

Figure 4.3‑2

Next is the example of Activity which uses In house only Content Provider.

Point (Using a Content Provider):

8.  Declare to use the in-house signature permission.
9.  Verify if the in-house signature permission is defined by an
    in-house application.0
10. Verify if the destination application is signed with the in-house certificate.
11. Sensitive information can be sent since the destination application
    is in-house one.
12. Handle the received result data carefully and securely, even though
    the data comes from an in-house application.
13. When exporting an APK, sign the APK with the same developer key as
    that of the destination application.

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Provider InhouseUser.providerInhouseUser.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```

InhouseUserActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Provider InhouseUser.InhouseUserActivity.java
   :language: java
   :encoding: shift-jis
```

SigPerm.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.SigPerm.java
   :language: java
   :encoding: shift-jis
```

PkgCert.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCert.java
   :language: java
   :encoding: shift-jis
```

\*\*\* Point 13 \*\*\* When exporting an APK, sign the APK with the
same developer key as that of the destination application.

![](media/image35.png)
```eval_rst
.. {width="4.647222222222222in"
.. height="3.2743055555555554in"}
```

Figure 4.3‑3

#### Creating/Using Temporary permit Content Providers

Temporary permit Content Provider is basically a private Content
Provider, but this permits the particular applications to access the
particular URI. By sending an Intent which special flag is specified
to the target applications, temporary access permission is provided to
those applications. Contents provider side application can give the
access permission actively to other applications, and it can also give
access permission passively to the application which claims the
temporary access permission.

Sample code of how to implement a temporary permit Content Provider is shown below.

Points (Creating a Content Provider):

1.  Explicitly set the exported attribute to false.
2.  Specify the path to grant access temporarily with the
    grant-uri-permission.
3.  Handle the received request data carefully and securely, even though
    the data comes from the application granted access temporarily.
4.  Information that is granted to disclose to the temporary access
    applications can be returned.
5.  Specify URI for the intent to grant temporary access.
6.  Specify access rights for the intent to grant temporary access.
7.  Send the explicit intent to an application to grant temporary access.
8.  Return the intent to the application that requests temporary access.

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Provider TemporaryProvider.app.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```

TemporaryProvider.java
```eval_rst
.. literalinclude:: CodeSamples/Provider TemporaryProvider.TemporaryProvider.java
   :language: java
   :encoding: shift-jis
```

TemporaryActiveGrantActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Provider TemporaryProvider.TemporaryActiveGrantActivity.java
   :language: java
   :encoding: shift-jis
```

TemporaryPassiveGrantActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Provider TemporaryProvider.TemporaryPassiveGrantActivity.java
   :language: java
   :encoding: shift-jis
```

Next is the example of temporary permit Content Provider.

Points (Using a Content Provider):

9. Do not send sensitive information.
10. When receiving a result, handle the result data carefully and securely.

TemporaryUserActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Provider TemporaryUser.TemporaryUserActivity.java
   :language: java
   :encoding: shift-jis
```

### Rule Book<!-- f7f5d0a0 -->

Be sure to follow the rules below when Implementing or using a content provider.

1.  Content Provider that Is Used Only in an Application Must Be Set as Private (Required)
2.  Handle the Received Request Parameter Carefully and Securely (Required)
3.  Use an In-house Defined Signature Permission after Verifying that it
    is Defined by an In-house Application (Required)
4.  When Returning a Result, Pay Attention to the Possibility of
    Information Leakage of that Result from the Destination Application (Required)
5.  When Providing an Asset Secondarily, the Asset should be Protected
    with the Same Level of Protection (Required)

And user side should follow the below rules, too.

6.  Handle the Returned Result Data from the Content Provider Carefully and Securely (Required)

#### Content Provider that Is Used Only in an Application Must Be Set as Private (Required)

Content Provider which is used only in a single application is not
necessary to be accessed by other applications, and the access which
attacks the Content Provider is not often considered by developers. A
Content Provider is basically the system to share data, so it\'s
handled as public by default. A Content Provider which is used only in
a single application should be set as private explicitly, and it
should be a private Content Provider. In Android 2.3.1 (API Level 9)
or later, a Content Provider can be set as private by specifying
android:exported=\"false\" in provider element.

AndroidManifest.xml
```xml
        <!-- *** POINT 1 *** Set false for the exported attribute explicitly. -->
        <provider
            android:name=".PrivateProvider"
            android:authorities="org.jssec.android.provider.privateprovider"
            android:exported="false" />
```

#### Handle the Received Request Parameter Carefully and Securely (Required)

Risks differ depending on the types of Content Providers, but when
processing request parameters, the first thing you should do is input
validation.

Although each method of a Content Provider has the interface which is
supposed to receive the component parameter of SQL statement, actually
it simply hands over the arbitrary character string in the system, so
it\'s necessary to pay attention that Contents Provider side needs to
suppose the case that unexpected parameter may be provided.

Since Public Content Providers can receive requests from untrusted
sources, they can be attacked by malware. On the other hand, Private
Content Providers will never receive any requests from other
applications directly, but it is possible that a Public Activity in
the targeted application may forward a malicious Intent to a Private
Content Provider so you should not assume that Private Content
Providers cannot receive any malicious input.

Since other Content Providers also have the risk of a malicious intent
being forwarded to them as well, it is necessary to perform input
validation on these requests as well.

Please refer to \"3.2 Handling Input Data Carefully and Securely\"

#### Use an In-house Defined Signature Permission after Verifying that it is Defined by an In-house Application (Required) <!-- 24dcfd7e -->

Make sure to protect your in-house Content Providers by defining an
in-house signature permission when creating the Content Provider.
Since defining a permission in the AndroidManifest.xml file or
declaring a permission request does not provide adequate security,
please be sure to refer to \"5.2.1.2 How to Communicate Between
In-house Applications with In-house-defined Signature Permission.\"

#### When Returning a Result, Pay Attention to the Possibility of Information Leakage of that Result from the Destination Application (Required) <!-- a9a47c42 -->

In case of query() or insert(), Cursor or Uri is returned to the
request sending application as a result information. When sensitive
information is included in the result information, the information may
be leaked from the destination application. In case of update() or
delete(), number of updated/deleted records is returned to the request
sending application as a result information. In rare cases, depending
on some application specs, the number of updated/deleted records has
the sensitive meaning, so please pay attention to this.

#### When Providing an Asset Secondarily, the Asset should be Protected with the Same Level of Protection (Required) <!-- 2db21801 -->

When an information or function asset, which is protected by a
permission, is provided to another application secondhand, you need to
make sure that it has the same required permissions needed to access
the asset. In the Android OS permission security model, only an
application that has been granted proper permissions can directly
access a protected asset. However, there is a loophole because an
application with permissions to an asset can act as a proxy and allow
access to an unprivileged application. Substantially this is the same
as re-delegating a permission, so it is referred to as the
\"Permission Re-delegation\" problem. Please refer to \"5.2.3.4
Permission Re-delegation Problem.\"

#### Handle the Returned Result Data from the Content Provider Carefully and Securely (Required)

Risks differ depending on the types of Content Provider, but when
processing a result data, the first thing you should do is input
validation.

In case that the destination Content Provider is a public Content
Provider, Malware which masquerades as the public Content Provider may
return the attack result data. On the other hand, in case that the
destination Content Provider is a private Content Provider, it is less
risk because it receives the result data from the same application,
but you should not assume that private Content Providers cannot
receive any malicious input. Since other Content Providers also have
the risk of a malicious data being returned to them as well, it is
necessary to perform input validation on that result data as well.

Please refer to \"3.2 Handling Input Data Carefully and Securely\"

Serviceを作る・利用する
-----------------------

### サンプルコード<!-- d4d5857c -->

Serviceがどのように利用されるかによって、Serviceが抱えるリスクや適切な防御手段が異なる。次の判定フローによって作成するServiceがどのタイプであるかを判断できる。なお、作成するServiceのタイプによってServiceを利用する側の実装も決まるので、利用側の実装についても合わせて説明する。

![](media/image43.png)
```eval_rst
.. {width="7.26875in" height="3.186301399825022in"}
```

図 4.4‑1

Serviceには複数の実装方法があり、その中から作成するServiceのタイプに合った方法を選択することになる。下表の縦の項目が本文書で扱う実装方法であり、5種類に分類した。表中のo印は実現可能な組み合わせを示し、その他は実現不可能もしくは困難なものを示す。

なお、Serviceの実装方法の詳細については、「4.4.3.2 Serviceの実装方法について」および各Serviceタイプのサンプルコード（表中で\*印の付いたもの）を参照すること。

表 4.4‑1
```eval_rst
================ ========= ========= ============= ===========
分類             | 非公開  | 公開    | パートナー  | 自社限定
                 | Service | Service | 限定Service | Service
================ ========= ========= ============= ===========
startService型   | **o\*** | o       | \-          | o
IntentService型  | o       | **o\*** | \-          | o
local bind型     | o       | \-      | \-          | \-
Messenger bind型 | o       | o       | \-          | **o\***
AIDL bind型      | o       | o       | **o\***     | o
================ ========= ========= ============= ===========
```
以下では表 4.4‑1中の\*印の組み合わせを使って各セキュリティタイプのServiceのサンプルコードを示す。

#### 非公開Serviceを作る・利用する

非公開Serviceは、同一アプリ内でのみ利用されるServiceであり、もっとも安全性の高いServiceである。

また、非公開Serviceを利用するには、クラスを指定する明示的Intentを使えば誤って外部アプリにIntentを送信してしまうことがない。

以下、startService型のServiceを使用した例を示す。

ポイント(Serviceを作る）：

1.  exported=\"false\"により、明示的に非公開設定する
2.  同一アプリからのIntentであっても、受信Intentの安全性を確認する
3.  結果を返す場合、利用元アプリは同一アプリであるから、センシティブな情報を返送してよい

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Service PrivateService.app.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```


PrivateStartService.java
```eval_rst
.. literalinclude:: CodeSamples/Service PrivateService.PrivateStartService.java
   :language: java
   :encoding: shift-jis
```


次に非公開Serviceを利用するActivityのサンプルコードを示す。

ポイント(Serviceを利用する）：

4.  同一アプリ内Serviceはクラス指定の明示的Intentで呼び出す
5.  利用先アプリは同一アプリであるから、センシティブな情報を送信してもよい
6.  結果を受け取る場合、同一アプリ内Serviceからの結果情報であっても、受信データの安全性を確認する

PrivateUserActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Service PrivateService.PrivateUserActivity.java
   :language: java
   :encoding: shift-jis
```


#### 公開Serviceを作る・利用する

公開Serviceは、不特定多数のアプリに利用されることを想定したServiceである。マルウェアが送信した情報（Intentなど）を受信することがあることに注意が必要である。また、公開Serviceを利用するには、送信する情報（Intentなど）がマルウェアに受信されることがあることに注意が必要である。

以下、IntentService型のServiceを使用した例を示す。

ポイント（Serviceを作る）：

1.  exported=\"true\"により、明示的に公開設定する
2.  受信Intentの安全性を確認する
3.  結果を返す場合、センシティブな情報を含めない

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Service PublicService.app.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```


PublicIntentService.java
```eval_rst
.. literalinclude:: CodeSamples/Service PublicService.PublicIntentService.java
   :language: java
   :encoding: shift-jis
```

次に公開Serviceを利用するActivityのサンプルコードを示す。

ポイント（Serviceを利用する）：

4.  センシティブな情報を送信してはならない
5.  結果を受け取る場合、結果データの安全性を確認する

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Service PublicServiceUser.app.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```

PublicUserActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Service PublicServiceUser.PublicUserActivity.java
   :language: java
   :encoding: shift-jis
```


#### パートナー限定Service

パートナー限定Serviceは、特定のアプリだけから利用できるServiceである。パートナー企業のアプリと自社アプリが連携してシステムを構成し、パートナーアプリとの間で扱う情報や機能を守るために利用される。

以下、AIDL bind型のServiceを使用した例を示す。

ポイント(Serviceを作る)：

1.  Intent Filterを定義せず、exported=\"true\"を明示的に設定する
2.  利用元アプリの証明書がホワイトリストに登録されていることを確認する
3.  onBind(onStartCommand,onHandleIntent)で呼び出し元がパートナーかどうか判別できない
4.  パートナーアプリからのIntentであっても、受信Intentの安全性を確認する
5.  パートナーアプリに開示してよい情報に限り返送してよい

なお、ホワイトリストに指定する利用先アプリの証明書ハッシュ値の確認方法は「[5.2.1.3 アプリの証明書のハッシュ値を確認する方法」を参照すること。

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Service PartnerServiceAIDL.servicePartnerServiceAIDL.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```


今回の例ではAIDLファイルを２つ作成する。１つは、ServiceからActivityにデータを渡すためのコールバックインターフェースで、もう１つはActivityからServiceにデータを渡し、情報を取得するインターフェースである。なお、AIDLファイルに記述するパッケージ名は、javaファイルに記述するパッケージ名と同様に、AIDLファイルを作成するディレクトリ階層に一致させる必要がある。

IPartnerAIDLServiceCallback.aidl
```java
package org.jssec.android.service.partnerservice.aidl;

interface IPartnerAIDLServiceCallback {
    /**
     * 値が変わった時に呼び出される
     */
    void valueChanged(String info);
}
```

IPartnerAIDLService.aidl
```java
package org.jssec.android.service.partnerservice.aidl;

import org.jssec.android.service.partnerservice.aidl.IExclusiveAIDLServiceCallback;

interface IPartnerAIDLService {

    /**
     * コールバックを登録する
     */
    void registerCallback(IPartnerAIDLServiceCallback cb);
    
    /**
     * 情報を取得する
     */     
    String getInfo(String param);

    /**
     * コールバックを解除する
     */
    void unregisterCallback(IPartnerAIDLServiceCallback cb);
}
```

PartnerAIDLService.java
```eval_rst
.. literalinclude:: CodeSamples/Service PartnerServiceAIDL.PartnerAIDLService.java
   :language: java
   :encoding: shift-jis
```

PkgCertWhitelists.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCertWhitelists.java
   :language: java
   :encoding: shift-jis
```

PkgCert.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCert.java
   :language: java
   :encoding: shift-jis
```


次にパートナー限定Serviceを利用するActivityのサンプルコードを示す。

ポイント(Serviceを利用する)：

6.  利用先パートナー限定Serviceアプリの証明書がホワイトリストに登録されていることを確認する
7.  利用先パートナー限定アプリに開示してよい情報に限り送信してよい
8.  明示的Intentによりパートナー限定Serviceを呼び出す
9.  パートナー限定アプリからの結果情報であっても、受信Intentの安全性を確認する

PartnerAIDLUserActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Service PartnerServiceAIDLUser.PartnerAIDLUserActivity.java
   :language: java
   :encoding: shift-jis
```


PkgCertWhitelists.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCertWhitelists.java
   :language: java
   :encoding: shift-jis
```


PkgCert.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCert.java
   :language: java
   :encoding: shift-jis
```


#### 自社限定Service

自社限定Serviceは、自社以外のアプリから利用されることを禁止するServiceである。複数の自社製アプリでシステムを構成し、自社アプリが扱う情報や機能を守るために利用される。

以下、Messenger bind型のServiceを使用した例を示す。

ポイント（Serviceを作る）：

1.  独自定義Signature Permissionを定義する
2.  独自定義Signature Permissionを要求宣言する
3.  Intent Filterを定義せず、exported=\"true\"を明示的に設定する
4.  独自定義Signature Permissionが自社アプリにより定義されていることを確認する
5.  自社アプリからのIntentであっても、受信Intentの安全性を確認する
6.  利用元アプリは自社アプリであるから、センシティブな情報を返送してよい
7.  利用元アプリと同じ開発者鍵でAPKを署名する

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Service InhouseServiceMessenger.serviceInhouseServiceMessenger.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```


InhouseMessengerService.java
```eval_rst
.. literalinclude:: CodeSamples/Service InhouseServiceMessenger.InhouseMessengerService.java
   :language: java
   :encoding: shift-jis
```

SigPerm.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.SigPerm.java
   :language: java
   :encoding: shift-jis
```

PkgCert.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCert.java
   :language: java
   :encoding: shift-jis
```


★ポイント7★APKをExportするときに、利用元アプリと同じ開発者鍵でAPKを署名する。

![](media/image34.png)
```eval_rst
.. {width="4.647222222222222in"
.. height="3.2743055555555554in"}
```

図 4.4‑2

次に自社限定Serviceを利用するActivityのサンプルコードを示す。

ポイント(Serviceを利用する)：

8.  独自定義Signature Permissionを利用宣言する
9.  独自定義Signature Permissionが自社アプリにより定義されていることを確認する
10.  利用先アプリの証明書が自社の証明書であることを確認する
11.  利用先アプリは自社アプリであるから、センシティブな情報を送信してもよい
12.  明示的Intentにより自社限定Serviceを呼び出す
13.  自社アプリからの結果情報であっても、受信Intentの安全性を確認する
14.  利用先アプリと同じ開発者鍵でAPKを署名する

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Service InhouseServiceMessengerUser.serviceInhouseServiceMessengerUser.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```

InhouseMessengerUserActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Service InhouseServiceMessengerUser.InhouseMessengerUserActivity.java
   :language: java
   :encoding: shift-jis
```

SigPerm.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.SigPerm.java
   :language: java
   :encoding: shift-jis
```

PkgCert.java
```eval_rst
.. literalinclude:: CodeSamples/JSSEC Shared.PkgCert.java
   :language: java
   :encoding: shift-jis
```


★ポイント14★APKをExportするときに、利用先アプリと同じ開発者鍵でAPKを署名する。

![](media/image34.png)
```eval_rst
.. {width="4.647222222222222in"
.. height="3.2743055555555554in"}
```

図 4.4‑3

### ルールブック<!-- b4210f43 -->

Service実装時には以下のルールを守ること。

1.  アプリ内でのみ使用するServiceは非公開設定する （必須）
1.  受信データの安全性を確認する （必須）
2.  独自定義Signature Permissionは、自社アプリが定義したことを確認して利用する （必須）
3.  連携するタイミングでServiceの機能を提供するかを判定する （必須）
4.  結果情報を返す場合には、返送先アプリからの結果情報漏洩に注意する （必須）
5.  利用先Serviceが固定できる場合は明示的IntentでServiceを利用する （必須）
6.  他社の特定アプリと連携する場合は利用先Serviceを確認する （必須）
7.  資産を二次的に提供する場合には、その資産の従来の保護水準を維持する （必須）
8.  センシティブな情報はできる限り送らない （推奨）

#### アプリ内でのみ使用するServiceは非公開設定する （必須）

アプリ内（または、同じUID）でのみ使用されるServiceは非公開設定する。これにより、他のアプリから意図せずIntentを受け取ってしまうことがなくなり、アプリの機能を利用される、アプリの動作に異常をきたす等の被害を防ぐことができる。

実装上はAndroidManifest.xmlでServiceを定義する際に、exported属性をfalseにするだけである。

AndroidManifest.xml
```xml
        <!-- 非公開Service -->
        <!-- ★ポイント1★ exported="false"により、明示的に非公開設定する -->
        <service android:name=".PrivateStartService" android:exported="false"/>
```

また、ケースは少ないと思われるが、同一アプリ内からのみ利用されるServiceであり、かつIntent
Filterを設置するような設計はしてはならない。Intent
Filterの性質上、同一アプリ内の非公開Serviceを呼び出すつもりでも、Intent
Filter経由で呼び出したときに意図せず他アプリの公開Serviceを呼び出してしまう場合が存在するからである。

AndroidManifest.xml(非推奨)
```xml
        <!-- 非公開Service -->
        <!-- ★ポイント1★ exported="false"により、明示的に非公開設定する -->
        <service android:name=".PrivateStartService" android:exported="false">
            <intent-filter>
                <action android:name=”org.jssec.android.service.OPEN />
            </intent-filter>
        </service>
```

「4.4.3.1 exported
設定とintent-filter設定の組み合わせ(Serviceの場合)」も参照すること。

#### 受信データの安全性を確認する （必須）

ServiceもActivityと同様に、受信Intentのデータを処理する際には、まず受信Intentの安全性を確認しなければならない。Serviceを利用する側もServiceからの結果(として受信した）情報の安全性を確認する必要がある。Activityの「4.1.2.5
受信Intentの安全性を確認する （必須）」「4.1.2.9
利用先Activityからの戻りIntentの安全性を確認する
（必須）」も参照すること。

Serviceにおいては、Intent以外にもメソッドの呼び出しやMessageによるデータの送受信などがあるため、それぞれ注意して実装を行わなければならない。

「3.2入力データの安全性を確認する」を参照すること。

#### 独自定義Signature Permissionは、自社アプリが定義したことを確認して利用する （必須）<!-- f8ccf894 -->

自社アプリだけから利用できる自社限定Serviceを作る場合、独自定義Signature
Permissionにより保護しなければならない。AndroidManifest.xmlでのPermission定義、Permission要求宣言だけでは保護が不十分であるため、「5.2
PermissionとProtection Level」の「5.2.1.2 独自定義のSignature
Permissionで自社アプリ連携する方法」を参照すること。

#### 連携するタイミングでServiceの機能を提供するかを判定する （必須）

Intentパラメータの確認や独自定義Signature
Permissionの確認といったセキュリティチェックをonCreateに入れてはいけない。その理由は、Serviceが起動中に新しい要求を受けたときにonCreateの処理が実施されないためである。したがって、startServiceによって開始されるServiceを実装する場合は、onStartCommand（IntentServiceを利用する場合はonHandleIntent）で判定を行わなければならない。bindServiceで開始するServiceを実装する場合も同様のことが言えるので、onBindで判定をしなければならない。

#### 結果情報を返す場合には、返送先アプリからの結果情報漏洩に注意する （必須）<!-- ee466beb -->

Serviceのタイプによって結果情報の返送先（コールバックの呼び出し先やMessageの送信先）アプリの信用度が異なる。返送先がマルウェアである可能性も考慮して十分に情報漏洩に対する配慮をしなければならない。

詳細は、Activityの「4.1.2.7
結果情報を返す場合には、返送先アプリからの結果情報漏洩に注意する
（必須）」を参照すること。

#### 利用先Serviceが固定できる場合は明示的IntentでServiceを利用する （必須）

暗黙的IntentによりServiceを利用すると、Intent
Filterの定義が同じ場合には先にインストールしたServiceにIntentが送信されてしまう。もし意図的に同じIntent
Filterを定義したマルウェアが先にインストールされていた場合、マルウェアにIntentが送信されてしまい、情報漏洩が生じる。一方、明示的IntentによりServiceを利用すると、指定したService以外がIntentを受信することはなく比較的安全である。

ただし、別途考慮すべき点があるので、Activityの「4.1.2.8
利用先Activityが固定できる場合は明示的IntentでActivityを利用する
（必須）」を参照すること。

#### 他社の特定アプリと連携する場合は利用先Serviceを確認する （必須）

他社の特定アプリと連携する場合にはホワイトリストによる確認方法がある。自アプリ内に利用先アプリの証明書ハッシュを予め保持しておく。利用先の証明書ハッシュと保持している証明書ハッシュが一致するかを確認することで、なりすましアプリにIntentを発行することを防ぐことができる。具体的な実装方法についてはサンプルコードセクション「4.4.1.3パートナー限定Service」を参照すること。

#### 資産を二次的に提供する場合には、その資産の従来の保護水準を維持する （必須）<!-- d4124f77 -->

Permissionにより保護されている情報資産および機能資産を他のアプリに二次的に提供する場合には、提供先アプリに対して同一のPermissionを要求するなどして、その保護水準を維持しなければならない。AndroidのPermissionセキュリティモデルでは、保護された資産に対するアプリからの直接アクセスについてのみ権限管理を行う。この仕様上の特性により、アプリに取得された資産がさらに他のアプリに、保護のために必要なPermissionを要求することなく提供される可能性がある。このことはPermissionを再委譲していることと実質的に等価なので、Permissionの再委譲問題と呼ばれる。「5.2.3.4　Permissionの再委譲問題」を参照すること。

#### センシティブな情報はできる限り送らない （推奨）<!-- 900385e7 -->

不特定多数のアプリと連携する場合にはセンシティブな情報を送ってはならない。

センシティブな情報をServiceと受け渡しする場合、その情報の漏洩リスクを検討しなければならない。公開Serviceに送付した情報は必ず漏洩すると考えなければならない。またパートナー限定Serviceや自社限定Serviceに送付した情報もそれらServiceの実装に依存して情報漏洩リスクの大小がある。

センシティブな情報はできるだけ送付しないように工夫すべきである。送付する場合も、利用先Serviceは信頼できるServiceに限定し、情報がLogCatなどに漏洩しないように配慮しなければならない。

### アドバンスト<!-- 78f16a9a -->

#### exported 設定とintent-filter設定の組み合わせ(Serviceの場合)

このガイド文書では、Serviceの用途から非公開Service、公開Service、パートナー限定Service、自社限定Serviceの4タイプのServiceについて実装方法を述べている。各タイプに許されているAndroidManifest.xmlのexported属性とintent-filter要素の組み合わせを次の表にまとめた。作ろうとしているServiceのタイプとexported属性およびintent-filter要素の対応が正しいことを確認すること。

表 4.4‑2
```eval_rst
+-------------------------+--------------------------------------------------------------+
|                         | exported属性の値                                             |
+                         +--------------------------------+--------------+--------------+
|                         | true                           | false        | 無指定       |
+=========================+================================+==============+==============+
| intent-filter定義がある | 公開                           | （使用禁止） | （使用禁止） |
+-------------------------+--------------------------------+--------------+--------------+
| intent-filter定義がない | 公開、パートナー限定、自社限定 | 非公開       | （使用禁止） |
+-------------------------+--------------------------------+--------------+--------------+

Serviceのexported属性が無指定である場合にそのServiceが公開されるか非公開となるかは、intent-filterの定義の有無により決まるが [12]_、本ガイドではServiceのexported属性を「無指定」にすることを禁止している。前述のようなAPIのデフォルトの挙動に頼る実装をすることは避けるべきであり、exported属性のようなセキュリティ上重要な設定を明示的に有効化する手段があるのであればそれを利用すべきであると考えられるためである。

.. [12] intent-filterが定義されていれば公開Service、定義されていなければ非公開Serviceとなる。
    https://developer.android.com/guide/topics/manifest/service-element.html\exported
    を参照のこと。
```

「intent-filter定義がある」&「exported="false"」を使用禁止にしているのは、Androidの振る舞いとして、同一アプリ内の非公開Serviceを呼び出したつもりでも、意図せず他アプリの公開Serviceを呼び出してしまう場合が存在するためである。

具体的には、Androidは以下のような振る舞いをするのでアプリ設計時に検討が必要である。

-   複数のServiceで同じ内容のintent-filterを定義した場合、先にインストールしたアプリ内のServiceの定義が優先される

-   暗黙的Intentを使った場合は、OSによって優先のServiceが自動的に選ばれて、呼び出される。

以下の3つの図でAndroidの振る舞いによる意図せぬ呼び出しが起こる仕組みを説明する。図
4.4‑4は、同一アプリ内からしか非公開Service(アプリA）を暗黙的Intentで呼び出せない正常な動作の例である。Intent-filter(図中action=\"X\")を定義しているのが、アプリAしかいないので意図通りの動きとなっている。

![](media/image44.png)
```eval_rst
.. {width="4.739583333333333in" height="2.9375in"}
```

図 4.4‑4

図 4.4‑5および図
4.4‑6は、アプリAに加えてアプリBでも同じintent-filter(図中action=\"X\")を定義している場合である。

図
4.4‑5は、アプリA→アプリBの順でインストールされた場合である。この場合、アプリCが暗黙的Intentを送信すると、非公開のService(A-1)を呼び出そうとして失敗する。一方、アプリAは暗黙的Intentを使って意図通りに同一アプリ内の非公開Serviceを呼び出せるので、セキュリティの(マルウェア対策の)面では問題は起こらない。

![](media/image45.png)
```eval_rst
.. {width="4.739583333333333in"
.. height="3.8020833333333335in"}
```

図 4.4‑5

図
4.4‑6は、アプリB→アプリAの順でインストールされた場合であり、セキュリティ面からみて問題がある。アプリAが暗黙的Intentを送信して同一アプリ内の非公開Serviceを呼び出そうとするが、先にインストールしたアプリBの公開Activity(B-1)が呼び出されてしまう例を示している。これによりアプリAからアプリBに対してセンシティブな情報を送信する可能性が生じてしまう。アプリBがマルウェアであれば、そのままセンシティブな情報の漏洩に繋がる。

![](media/image46.png)
```eval_rst
.. {width="4.739583333333333in"
.. height="3.8020833333333335in"}
```

図 4.4‑6

このように、Intent
Filterを用いた非公開Serviceの暗黙的Intent呼び出しは、意図せぬアプリの呼び出しや意図せぬアプリへのセンシティブな情報の送信を避けるためにも行うべきではない。

#### Serviceの実装方法について

Serviceの実装方法は多様であり、サンプルコードで分類したセキュリティ上のタイプとの相性もあるため簡単に特徴を示す。startServiceを利用する場合とbindServiceを利用する場合とに大きく分かれるが、startServiceとbindServiceの両方で利用できるServiceを作成することも可能である。Serviceの実装方法を決定するために、次のような項目について検討を行うことになる。

-   Serviceを別アプリに公開するか（Serviceの公開）

-   実行中にデータのやり取りを行うか(データの相互送受信)

-   Serviceを制御するか（起動や終了など）

-   別プロセスとして実行するか（プロセス間通信）

-   複数の処理を同時に行うか(並行処理)

実装方法の分類と各々の項目の実現の可否を表にすると、表
4.4‑3のようになる。xは実現不可能かもしくは提供される機能とは別の枠組みが必要な場合を表す。

表 4.4‑3 Serviceの実装方法の分類
```eval_rst
=============== ========== ============= ================ =========== ==========
分類            | Service  | データの    | Serviceの制御  | プロセス  並行処理
                | の公開   | 相互送受信  | (起動・終了)   | 間通信
=============== ========== ============= ================ =========== ==========
startService型  | o        | x           | o              | o          x
IntentService型 | o        | x           | x              | o          x
localbind型     | x        | o           | o              | x          x
Messengerbind型 | o        | o           | o              | o          x
AIDLbind型      | o        | o           | o              | o          o
=============== ========== ============= ================ =========== ==========
```

##### startService型

最も基本的なServiceである。Serviceクラスを継承し、onStartCommandで処理を行うServiceのことを指す。

利用する側は、Service
をIntentで指定してstartServiceを使用して呼び出す。Intentの送信元に対して、結果などのデータを直接返すことはできないため、Broadcastなど別の方法を組み合わせて実現する必要がある。具体的な実装例は、「4.4.1.1非公開Serviceを作る・利用する」を参照のこと。

セキュリティ上のチェックはonStartCommandで行う必要があるが、送信元のパッケージ名が取得できないためパートナー限定Serviceには使用できない。

##### IntentService型

IntentServiceはServiceを継承して作られているクラスである。呼び出し方は、startService型と同様である。通常のService（startService型）に比べて以下の特徴がある。

-   Intentの処理はonHandleIntentで行う。（onStartCommandは使わない）

-   別スレッドで実行される

-   処理がキューイングされる

処理が別スレッドのため呼び出しは即座に返され、キューイング機構によりシーケンシャルにIntentに対する処理が行われる。各Intentの並行処理はされないが、製品の要件によっては実装の簡素化の一つとして選択が可能である。Intentの送信元に対して、結果などのデータを直接返すことはできないため、Broadcastなど別の方法を組み合わせて実現する必要がある。具体的な実装例は、「4.4.1.2公開Serviceを作る・利用する」を参照のこと。

セキュリティ上のチェックはonHandleIntentで行う必要があるが、送信元のパッケージ名が取得できないためパートナー限定Serviceには使用できない。

##### local bind型

アプリと同じプロセス内でのみ動くローカルServiceを実装するための方法を指す。Binderクラスから派生したクラスを定義して、Serviceで実装した機能（メソッド）を呼び出し元に提供できるようにする。

利用する側は、Service
をIntentで指定してbindServiceを使用して呼び出す。Serviceをbindする方法の中では、最もシンプルな実装であるが、別プロセスでの起動やServiceの公開ができないため用途は限定される。具体的な実装例は、サンプルコードに含まれるプロジェクト「Service
PrivateServiceLocalBind」を参照のこと。

セキュリティ的には非公開Serviceのみ実装可能である。

##### Messenger bind型

Messengerの仕組みを利用してServiceとの連携を実現する方法を指す。

Serviceを利用する側からもMessageの返信先としてMessengerを渡すことができるため、双方でのデータのやり取りが比較的容易に実現可能である。また、処理はキューイングされるため、スレッドセーフに動作する特徴がある。各Messageの並行処理はされないが、製品の要件によっては実装の簡素化の一つとして選択が可能である。利用する側は、Service
をIntentで指定してbindServiceを使用して呼び出す。具体的な実装例は、「4.4.1.4自社限定Service」を参照のこと。

セキュリティ上のチェックはonBindやMessage
Handlerで行う必要があるが、送信元のパッケージ名が取得できないためパートナー限定Serviceには使用できない。

##### AIDL bind型

AIDLの仕組みを利用してServiceとの連携を実現する方法を指す。AIDLによってインターフェースを定義し、Serviceの持つ機能をメソッドとして提供する。また、AIDLで定義したインターフェースを利用側で実装することで、コールバックを実現することもできる。マルチスレッド呼び出しは可能だが、排他処理はされないのでService側で明示的に実装する必要がある。

利用する側は、Service
をIntentで指定してbindServiceを使用して呼び出す。具体的な実装例は、「4.4.1.3パートナー限定Service」を参照のこと。

セキュリティ上のチェックは自社限定ServiceではonBindで、パートナー限定ServiceではAIDLで定義したインターフェースの各メソッドで行う必要がある。本文書で分類した全セキュリティタイプのServiceに利用可能である。

SQLiteを使う
------------

本文書ではSQLiteを使用してデータベースの作成および操作を行う際にセキュリティ上で注意すべき点をまとめる。主なポイントは、データベースファイルのアクセス権の適切な設定と
SQL
インジェクションに対する対策である。ここでは、直接外部からデータベースファイルの読み書きを許す(複数アプリで共有する)ようなデータベースはここでは想定せずContent
Provider
のバックエンドやアプリ単体での使用を前提とする。また、ある程度センシティブな情報を扱っていることを想定しているが、そうでない場合も他アプリからの想定外の読み書きを避けるためにもここで挙げる対策を適用することをお勧めする。

### サンプルコード<!-- b48a65ad -->

#### データベースの作成と操作　
```eval_rst
Androidのアプリでデータベースを扱う場合、SQLiteOpenHelperを使用することでデータベースファイルの適切な配置およびアクセス権の設定（他のアプリがアクセスできない設定）ができる [13]_。ここでは、アプリ起動時にデータベースを作成し、UI上からデータの検索・追加・変更・削除を行う簡単なアプリを例に、外部からの入力に対して不正なSQLが実行されないようにSQLインジェクション対策したサンプルコードを示す。

.. [13] ファイルの配置に関しては、SQLiteOpenHelperのコンストラクタの第2引数（name）にファイルの絶対パスも指定できる。そのため、誤ってSDカードを直接指定した場合には他のアプリからの読み書きが可能になるので注意が必要である。
```
![](media/image47.png)
```eval_rst
.. {width="5.541666666666667in"
.. height="4.395833333333333in"}
```

図 4.5‑1

ポイント：

1.  データベース作成にはSQLiteOpenHelperを使用する
2.  SQLインジェクションの対策として入力値をSQL文に使用する場合にはプレースホルダを利用する
3.  SQLインジェクションの保険的な対策としてアプリ要件に従って入力値をチェックする

SampleDbOpenHelper.java
```eval_rst
.. literalinclude:: CodeSamples/SQLite Database.SampleDbOpenHelper.java
   :language: java
   :encoding: shift-jis
```

DataSearchTask.java （SQLite Databaseプロジェクト）
```eval_rst
.. literalinclude:: CodeSamples/SQLite Database.DataSearchTask.java
   :language: java
   :encoding: shift-jis
```


DataValidator.java
```eval_rst
.. literalinclude:: CodeSamples/SQLite Database.DataValidator.java
   :language: java
   :encoding: shift-jis
```


### ルールブック<!-- 48e87243 -->

SQLiteを使用する際には以下のルールを守ること。

1.  DBファイルの配置場所、アクセス権を正しく設定する （必須）

1.  他アプリとDBデータを共有する場合はContent Providerでアクセス制御する （必須）

2.  DB 操作時に可変パラメータを扱う場合はプレースホルダを使用する （必須）



#### DBファイルの配置場所、アクセス権を正しく設定する （必須）

DBファイルのデータの保護を考えた場合、DBファイルの配置場所とアクセス権の設定は合わせて考慮すべき重要な要素である。

例えば、ファイルのアクセス権を正しく設定したつもりでも、SD
カードなどアクセス権の設定を行えない場所に配置している場合には、誰からでもアクセス可能なDBファイルになってしまう。また、アプリディレクトリに配置した場合でも、アクセス権を正しく設定しないと意図しないアクセスを許してしまうことになる。ここでは、配置場所とアクセス権設定について守るべき点を挙げた後、それを実現するための方法について説明する。

まず配置場所とアクセス権設定については、DBファイル(データ)を保護する観点から考えると、以下の2点を実施する必要がある。
```eval_rst
1. 配置場所

Context\#getDatabasePath(String name)で取得できるファイルパスや場合によってはContext\#getFilesDir で取得できるディレクトリの場所に配置する [14]_

2. アクセス権

MODE_PRIVATE（=ファイルを作成したアプリのみがアクセス可能）モードに設定する

.. [14] どちらのメソッドも該当するアプリだけが読み書き権限を与えられ、他のアプリからはアクセスができないディレクトリ（パッケージディレクトリ）のサブディレクトリ以下のパスが取得できる。
```
この2点を実施することで、他のアプリからアクセスできないDBファイルの作成を行うことができる。これらを実施するためには以下の方法が挙げられる。

1\. SQLiteOpenHelperを使用する

2\. Context\#openOrCreateDatabaseを使用する

DBファイルの作成に際しては、SQLiteDatabase\#openOrCreateDatabaseを使用することもできる。しかし、このメソッドを使用した場合、Androidスマートフォンの機種によっては、他のアプリから読み取り可能なDBファイルが作成されることが分かっている。そのため、このメソッドの使用は避けて、他の方法を利用することを推奨する。上に挙げた2つの方法について、それぞれの特徴を以下で説明する。

##### SQLiteOpenHelperを使用する
```eval_rst
SQLiteOpenHelperを使用する場合、開発者はあまり多くのことを考えなくてもよい。SQLiteOpenHelperを派生したクラスを作成し、コンストラクタの引数にDBの名前（ファイル名に使われる） [15]_ を指定すれば、自動的に上記のセキュリティ要件を満たすDBファイルを作成してくれる。

「4.5.1.1 データベースの作成と操作」に具体的な使用方法を示しているので参照すること。

.. [15] （ドキュメントに記述はないが）SQLiteOpenHelper
    の実装ではDBの名前にはファイルのフルパスを指定できるので、SDカードなどアクセス権の設定できない場所のパスが意図せず入力されないように注意が必要である。
```
##### Context\#openOrCreateDatabaseを使用する

Context\#openOrCreateDatabaseメソッドを使用してDBの作成を行う場合、ファイルのアクセス権をオプションで指定する必要があり、明示的にMODE\_PRIVATEを指定する。

ファイルの配置に関しては、DB名（ファイル名に使用される）の指定をSQLiteOpenHelperと同様に行えるので、自動的に前述のセキュリティ要件を満たすファイルパスにファイルが作成される。ただし、フルパスも指定できるのでSDカードなどを指定した場合、MODE\_PRIVATEを指定しても他アプリからアクセス可能になってしまうため注意が必要である。

DBに対して明示的にアクセス許可設定を行う例：MainActivity.java
```java
public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //データベースの構築
        try {
           //MODE_PRIVATEを設定してDBを作成
           db = Context.openOrCreateDatabase("Sample.db", 
                                                     MODE_PRIVATE, null);
        } catch (SQLException e) {
            //データベース構築に失敗した場合ログ出力
            Log.e(this.getClass().toString(), getString(R.string.DATABASE_OPEN_ERROR_MESSAGE));
            return;
        }
        //省略 その他の初期化処理
    }
```
```eval_rst
なお、アクセス権の設定はMODE_PRIVATEと合わせて以下の3種類があり、MODE\_WORLD\_READABLEとMODE\_WORLD\_WRITEABLEはOR演算で同時指定することもできる。ただし、MODE\_PRIVATE以外はAPI Level 17以降ではdeprecatedとなっており、API Level 24 以降ではセキュリティ例外が発生する。API Level 15以降を対象とする場合でも、通常はこのフラグを使用しないことが望ましい [16]_。

-   MODE\_PRIVATE 作成アプリのみ読み書き可能

-   MODE\_WORLD\_READABLE 作成アプリは読み書き可能、他は読み込みのみ

-   MODE\_WORLD\_WRITEABLE 作成アプリは読み書き可能、他は書き込みのみ

.. [16] MODE\_WORLD\_READABLEおよびMODE\_WORLD\_WRITEABLEの性質と注意点については、「4.6.3.2
    ディレクトリのアクセス権設定」を参照
```
#### 他アプリとDBデータを共有する場合はContent Providerでアクセス制御する （必須）

他のアプリとDBデータを共有する手段として、DBファイルをWORLD\_READABLE、WORLD\_WRITABLEとして作成し、他のアプリから直接アクセスできるようにするという方法がある。しかし、この方法ではDBにアクセスするアプリやDBへの操作を制限できないため、意図しない相手（アプリ）にデータを読み書きされることもある。結果として、データの機密性や整合性に問題が生じたり、マルウェアの攻撃対象となったりする可能性も考えられる。

以上のことから、AndroidにおいてDBデータを他のアプリと共有する場合は、Content
Providerを使うことを強くお勧めする。Content
Providerを使うことにより、DBに対するアクセス制御を実現できるというセキュリティの観点からのメリットだけでなく、DBスキーマ構造をContent
Provider内に隠ぺいできるといった設計観点のメリットもある。

#### DB 操作時に可変パラメータを扱う場合はプレースホルダを使用する （必須）

SQLインジェクションを防ぐという意味で、任意の入力値を　SQL文に組み込む時はプレースホルダを使用するべきである。プレースホルダを使用したSQLの実行方法としては以下の2つの方法を挙げることができる。

1.  SQLiteDatabase\#compileStatement()を使用して SQLiteStatement
    を取得する。その後、
    SQLiteStatement\#bindString()、bindLong()などを使用してパラメータをプレースホルダに配置する

2.  SQLiteDatabeseクラスのexecSQL()、insert()、update()、delete()、query()、rawQuery()、replace()などを呼び出す際にプレースホルダを持ったSQL文を使用する

なお、SQLiteDatabase\#compileStatement()を使用して、SELECT
コマンドを実行する場合、

「SELECTコマンドの結果として先頭の1要素(1行1列目)しか取得できない」

という制限があるので用途が限られる。

どちらの方式を使う場合でも、プレースホルダに与えるデータの内容は事前にアプリ要件に従ってチェックされていることが望ましい。以下で、それぞれの方法について説明する。

##### SQLiteDatabase\#compileStatement()を使用する場合：

以下の手順でプレースホルダへデータを渡す。

1.  SQLiteDatabase\#compileStatement()を使用してプレースホルダを含んだSQL文をSQLiteStatementとして取得する。

2.  作成したSQLiteStatementオブジェクトに対して、bindLong()、bindString()などのメソッドを使用してプレースホルダに設定する。

3.  SQLiteStatementオブジェクトのexecute()などのメソッドによってSQLを実行する。

プレースホルダ使用例：DataInsertTask.java（抜粋）
```java
//データ追加タスク
public class DataInsertTask extends AsyncTask<String, Void, Void> {
    private MainActivity    mActivity;
    private SQLiteDatabase  mSampleDB;

    public DataInsertTask(SQLiteDatabase db, MainActivity activity) {
        mSampleDB = db;
        mActivity = activity;
    }

    @Override
    protected Void doInBackground(String... params) {
        String  idno = params[0];
        String  name = params[1];
        String  info = params[2];

        //★ポイント3★ アプリケーション要件に従って入力値をチェックする
       if (!DataValidator.validateData(idno, name, info))
        {
        	return null;
        }
        //データ追加処理
        //プレースホルダを使用する
        String commandString = "INSERT INTO " + CommonData.TABLE_NAME + " (idno, name, info) VALUES (?, ?, ?)";
        SQLiteStatement sqlStmt = mSampleDB.compileStatement(commandString);
        sqlStmt.bindString(1, idno);
        sqlStmt.bindString(2, name);
        sqlStmt.bindString(3, info);
        try {
            sqlStmt.executeInsert();
        } catch (SQLException e) {
            Log.e(DataInsertTask.class.toString(), mActivity.getString(R.string.UPDATING_ERROR_MESSAGE));
        } finally {
        	sqlStmt.close();
        }
        return null;
    }

    // ～省略～
}
```

あらかじめ実行するSQL文をオブジェクトとして作成しておきパラメータを当てはめる形である。実行する処理が確定しているので、SQLインジェクションが発生する余地はない。また、SQLiteStatementオブジェクトを再利用することで処理効率を高めることができるというメリットもある。

##### SQLiteDatabaseが提供する各処理用のメソッドを使用する場合：

SQLiteDatabaseが提供するDB操作メソッドには、SQL文を使用するものとそうでないものがある。SQL文を使用するメソッドにSQLiteDatabase\#
execSQL()/rawQuery()などがあり、以下の手順で実行する。

1.  プレースホルダを含んだSQL文を用意する。

2.  プレースホルダに割り当てるデータを作成する。

3.  SQL文とデータを引数として渡して処理用メソッドを実行する。

一方、SQL文を使用しないメソッドには、SQLiteDatabase\#insert()/update()/delete()/query()/replace()などがある。これらを使用する場合には、以下の手順でデータを渡す。

1.  DBに対して挿入/更新するデータがある場合には、ContentValuesに登録する。

2.  ContentValuesを引数として渡して、各処理用メソッド（以下の例ではSQLiteDatabase\#insert()）を実行する。

各処理用メソッド（SQLiteDatabase\#insert()）を使用する例
```java
    private SQLiteDatabase  mSampleDB;
    private void addUserData(String idno, String name, String info) {

       //値の妥当性（型、範囲）チェック、エスケープ処理
       if (!validateInsertData(idno, name, info)) {
           //バリデーションを通過しなかった場合、ログ出力
           Log.e(this.getClass().toString(), getString(R.string.VALIDATION_ERROR_MESSAGE));
           return 
       }

        //挿入するデータの準備
        ContentValues insertValues = new ContentValues();
        insertValues.put("idno", idno);
        insertValues.put("name", name);
        insertValues.put("info", info);

        //Insert実行
        try {
            mSampleDb.insert("SampleTable", null, insertValues);
        } catch (SQLException e) {
            Log.e(this.getClass().toString(), getString(R.string.DB_INSERT_ERROR_MESSAGE));
            return;
        }
    }
```

この例では、SQLコマンドを直接記述せず、SQLiteDatabaseが提供する挿入用のメソッドを使用している。SQLコマンドを直接使用しないため、この方法もSQLインジェクションの余地はないと言える。

### アドバンスト<!-- 6461be2f -->

#### SQL文のLIKE述語でワイルドカードを使用する際にエスケープ処理を施す

LIKE述語のワイルドカード（%、\_）を含む文字列をプレースホルダの入力値として使用した場合、そのままだとワイルドカードとして機能するため、必要に応じて事前にエスケープ処理を施す必要がある。必要なケースとしてはワイルドカードを単体の文字（\"%\"や\"\_\"）として扱いたい場合が当てはまる。

実際のエスケープ処理は、以下のサンプルコードのようにESCAPE句を使用して行うことができる。

LIKEを利用した場合のエスケープ処理の例
```java
//データ検索タスク
public class DataSearchTask extends AsyncTask<String, Void, Cursor> {
    private MainActivity        mActivity;
    private SQLiteDatabase      mSampleDB;
    private ProgressDialog      mProgressDialog;

    public DataSearchTask(SQLiteDatabase db, MainActivity activity) {
        mSampleDB = db;
        mActivity = activity;
    }

    @Override
    protected Cursor doInBackground(String... params) {
        String  idno = params[0];
        String  name = params[1];
        String  info = params[2];
        String  cols[]  =   {"_id", "idno","name","info"};

        Cursor cur;

        // ～省略～

        //infoを条件にしてlike検索（部分一致）
        //ポイント：ワイルドカードに相当する文字はエスケープ処理する
        String argString = info.replaceAll("@", "@@"); //入力として受け取ったinfo内の$をエスケープ
        argString = argString.replaceAll("%", "@%"); //入力として受け取ったinfo内の%をエスケープ
        argString = argString.replaceAll("_", "@_"); //入力として受け取ったinfo内の_をエスケープ
        String selectionArgs[] = {argString};

        try {
            //ポイント：プレースホルダを使用する
            cur = mSampleDB.query("SampleTable", cols, "info LIKE '%' || ? || '%' ESCAPE '@'", 
                                   selectionArgs, null, null, null);
        } catch (SQLException e) {
            Toast.makeText(mActivity, R.string.SERCHING_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
            return null;
        }
        return cur;
    }
    
    @Override
    protected void onPostExecute(Cursor resultCur) {
        mProgressDialog.dismiss();
        mActivity.updateCursor(resultCur);
    }
}
```

#### プレースホルダを使用できないSQLコマンドに対して外部入力を使う

テーブルの作成や削除などのDBオブジェクトを処理対象としたSQL文を実行する場合、テーブル名などの値に対してプレースホルダを使うことはできない。基本的には、プレースホルダの使用できない値に対して、外部から入力された任意の文字列を使用するようなデータベースの設計はすべきでない。

仕様や機能上の制限でプレースホルダを使用できない場合は、入力値に危険が無いかどうか実行前に確認し、必要な処理を施すことが必須となる。

基本的には、

1.  文字列パラメータとして使用する場合、文字のエスケープやクォート処理を施す

2.  数値パラメータとして使用する場合、数字以外の文字が混入していないことを確認する

3.  識別子、コマンドとして使用する場合、1．に加え、使用できない文字が含まれていないことを確認する

を実施する。

> 参照：[http://www.ipa.go.jp/security/vuln/documents/website\_security\_sql.pdf](http://www.ipa.go.jp/security/vuln/documents/website_security_sql.pdf)

#### 不用意にデータベースの書き換えが行われないための対策を行う
```eval_rst
SQLiteOpenHelper\#getReadableDatabase、getWritableDatabaseを使用してDBのインスタンスを取得した場合、どちらのメソッドを利用してもDBは読み書き可能な状態でオープンされる [17]_。また、Context\#openOrCreateDatabase、SQLiteDatabase\#openOrCreateDatabaseなども同様である。

.. [17] getReableDatabase
    は基本的にはgetWritableDatabaseで取得するのと同じオブジェクトを返す。ディスクフルなどの状況で書き込み可能オブジェクトを生成できない場合にリードオンリーのオブジェクトを返すという仕様である（getWritableDatabaseはディスクフルなどの状況では実行エラーとなる）。
```
これは、アプリ操作や実装の不具合により意図せずDBの中身を書き換えてしまう（書き換えられてしまう）可能性を意味している。基本的にはアプリの仕様と実装の範囲で対応できると考えられるが、アプリの検索機能など、読み取りしか必要のない機能を実装する場合は、データベースを読み取り専用でオープンすることで、設計や検証の簡素化ひいてはアプリ品質の向上に繋がる場合があるので、状況に応じて検討をお勧めする。

具体的には、SQLiteDatabase\#openDatabaseにOPEN\_READONLYを指定してデータベースをオープンする。

読み取り専用でデータベースをオープンする
```java
    // ～省略～

    // データベースのオープン(データベースは作成済みとする)
    SQLiteDatabase db 
           = SQLiteDatabase.openDatabase(SQLiteDatabase.getDatabasePath("Sample.db"), null, OPEN_READONLY);
```

> 参照：[http://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper.html - getReadableDatabase()](http://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper.html#getReadableDatabase())

#### アプリの要件に従ってDBの入出力データの妥当性をチェックする

SQLiteは型に寛容なデータベースであり、DB上でIntegerとして宣言されているカラムに対して文字型のデータを格納することが可能である。DB内のデータは、数値型を含む全てのデータが平文の文字データとしてDB内に格納されている。このため、Integer型のカラムに対して文字列型の検索（　LIKE
'%123%' など）を行うことも可能である。また、VARCHAR(100)
のようにデータの最大長を記述してもそれ以上の長さのデータが入力可能であるなど、SQLiteでの値の制限（正当性確認）は期待できない。

このため、SQLiteを使用するアプリは、このようなDBの特性に注意して予期せぬデータをDBに格納したり取得したりしないようにアプリの要件に従って対処する必要がある。対処の方法としては次の2つがある。

1.  データをデータベースに格納する際、型や長さなどの条件が一致しているか確認する

2.  データベースから値を取得した際、データが想定外の型や長さでないか確認する

以下では、例として入力値が1以上の数字であることを検証するコードを示す。

例：入力データが1以上の数字であることを確認する（MainActivity.javaより抜粋）
```java
public class MainActivity extends Activity {

    // ～省略～

    //追加処理
    private void addUserData(String idno, String name, String info) {
        //Noのチェック
        if (!validateNo(idno, CommonData.REQUEST_NEW)) {
            return;
        }

        //データ追加処理
        DataInsertTask task = new DataInsertTask(mSampleDb, this);
        task.execute(idno, name, info);        
    }

    // ～省略～

    private boolean validateNo(String idno, int request) {
        if (idno == null || idno.length() == 0) {
            if (request == CommonData.REQUEST_SEARCH) {
                //検索処理の時は未指定をOKにする
                return true;
            } else {   
                //検索処理以外の時はnull、空文字はエラー
                Toast.makeText(this, R.string.IDNO_EMPTY_MESSAGE, Toast.LENGTH_LONG).show();
                return false;
            }
        }

        //数字であることを確認する
        try {
            // 1以上の値
            if (!idno.matches("[1-9][0-9]*")) {
                //数字以外の時はエラー
                Toast.makeText(this, R.string.IDNO_NOT_NUMERIC_MESSAGE, Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (NullPointerException e) {
            //今回のケースではあり得ない
            return false;
        }

        return true;
    }

    // ～省略～
}
```

#### DBに格納するデータについての考察

SQLiteでは、データをファイルに格納する際に以下のような実装になっている。

-   数値型を含む全てのデータが平文の文字データとしてDBファイル内に格納される

-   DBに対してデータの削除を行ってもデータ自体はDBファイルから削除されない（削除マークが付くのみ）

-   データを更新した場合もDBファイル内には更新前のデータも削除されず残っている

よって、削除された「はず」の情報がDBファイル内に残ったままの状態になっている可能性がある。この場合でも、本文書に従って対策を施し、Android
のセキュリティ機能が有効であれば、他アプリを含む第三者からデータ・ファイルに直接アクセスされる心配はない。ただし、root権限を奪取されるなどAndroidの保護機構を迂回してファイルを抜き出される可能性を考えると、ビジネスに大きな影響を与えるデータが格納されている場合には、Android保護機構に頼らないデータ保護も検討しなければならない。

これらの理由により、端末のroot権限が奪取された場合でも守る必要があるような重要なデータはSQLiteのDBにそのまま格納すべきではない。どうしても重要なデータを格納せざるを得ない場合には暗号化したデータを格納する、DB全体を暗号化する、などの対策が必要となる。

実際に暗号化が必要な場合、暗号化に使う鍵の扱いやコードの難読化など本文書の範囲を超える課題が多いので、現時点でビジネスインパクトの大きなデータを扱うアプリの開発には専門家への相談をお勧めする。

参考として「4.5.3.6 \[参考\]SQLiteデータベースを暗号化する(SQLCipher for
Android)」に、データベースを暗号化するライブラリを紹介しておく。

#### \[参考\]SQLiteデータベースを暗号化する(SQLCipher for Android)

SQLCipherは、データベースファイルの透過的な256ビットAESの暗号化を提供するSQLite拡張である。現在は、オープンソース(BSDライセンス)化され、Zetetic
LLCによって維持・管理されている。モバイルの世界では、SQLCipherは、ノキア/QT
、アップルのiOSで広く使用されている。

SQLCipher for
Androidプロジェクトは、Android環境におけるSQLiteデータベースの標準の統合化された暗号化をサポートすることを目的としている。標準のSQLiteのAPIをSQLCipher用に作成することで、開発者は通常と同じコーディングで暗号化されたデータベースを利用できるようになっている。

参照：[https://guardianproject.info/code/sqlcipher/](https://guardianproject.info/code/sqlcipher/)

##### 使い方

アプリ開発者は以下の３つの作業をすることでSQLCipherの利用が可能になる。

1.  アプリの lib ディレクトリに sqlcipher.jar
    および、libdatabase\_sqlcipher.so、libsqlcipher\_android.so、libstlport\_shared.soを配置する。

2.  全てのソースファイルについて、import で指定されている
    android.database.sqlite.\*
    を全てinfo.guardianproject.database.sqlite.\*
    に変更する。なお、android.database.Cursorはそのまま使用可能である。

3.  onCreate()の中でデータベースを初期化し、データベースをオープンする際にパスワードを設定する。

簡単なコード例
```java
SQLiteDatabase.loadLibs(this);                   //まず ライブラリをContextを使用して初期化する
SQLiteOpenHelper.getWritableDatabase(passwoed):  //引数はパスワード（String型 セキュアに取得したものと仮定）
```

SQLiteDatabase.loadLibs(this); //まず
ライブラリをContextを使用して初期化する

SQLiteOpenHelper.getWritableDatabase(passwoed):
//引数はパスワード（String型 セキュアに取得したものと仮定）

SQLCipher for
Androidは執筆時点でバージョン1.1.0であり、2.0.0版が開発進行中でRC4が公開されている状況である。Android
における使用実績やAPIの安定性という点で今後検証が必要となるが、現時点でAndroidで利用可能なSQLiteの暗号化ソリューションとして検討する余地はある。

##### ライブラリ構成

SQLCipherを使用するためにはSDKとして含まれている以下のファイルが必要となる。

-   assets/icudt46l.zip 2,252KB<br/>
    端末の /system/usr/icu/ 以下にicudt46l.datが存在しない場合に必要となる。<br/>
    icudt46l.datが見つからない場合、このzipが解凍されて使用される。
-   libs/armeabi/libdatabase\_sqlcipher.so 44KB
-   libs/armeabi/libsqlcipher\_android.so 1,117KB
-   libs/armeabi/libstlport\_shared.so 555KB<br/>
    Nativeライブラリ。<br/>
    SQLCipherの初期ロード時（SQLiteDatabase\#loadLibs()呼び出し時）に読み込まれる。
-   libs/commons-codec.jar 46KB
-   libs/guava-r09.jar 1,116KB
-   libs/sqlcipher.jar 102KB<br/>
    Nativeライブラリを呼び出すJavaライブラリ。<br/>
    sqlcipher.jarがメイン。あとはsqlcipher.jarから参照されている。

合計：約5.12MB

ただし、icudt46l.zipは解凍されると7MB程度になる。

ファイルを扱う
--------------

Androidのセキュリティ設計思想に従うと、ファイルは情報を永続化又は一時保存(キャッシュ)する目的にのみ利用し、原則非公開にするべきである。アプリ間の情報交換はファイルを直接アクセスさせるのではなく、ファイル内の情報をContent
ProviderやServiceといったアプリ間連携の仕組みによって交換するべきである。これによりアプリ間のアクセス制御も実現できる。

SDカード等の外部記憶デバイスは十分なアクセス制御ができないため、容量の大きなファイルを扱う場合や別の場所(PCなど)への情報の移動目的など、機能上どうしても必要な場合のみに使用を限定するべきである。基本的に外部記憶デバイス上にはセンシティブな情報を含んだファイルを配置してはならない。もしセンシティブな情報を外部記憶デバイス上のファイルに保存しなければならない場合は暗号化等の対策が必要になるが、ここでは言及しない。

### サンプルコード<!-- 267a2965 -->

前述のようにファイルは原則非公開にするべきである。しかしながらさまざまな事情によって、他のアプリにファイルを直接読み書きさせるべきときもある。セキュリティの観点から分類したファイルの種類と比較を表
4.6‑1に示す。ファイルの格納場所や他アプリへのアクセス許可の組み合わせにより4種類のファイルに分類している。以降ではこのファイルの分類ごとにサンプルコードを示し説明を加えていく。

表
4.6‑1　セキュリティ観点によるファイルの分類と比較
```eval_rst
+-----------+-----------+-------------+------------------------------------------+
|| ファイル || 他アプリ | 格納場所    || 概要                                    |
|| の分類   || へのアク |             |                                          |
|           || セス許可 |             |                                          |
+===========+===========+=============+==========================================+
|| 非公開   || なし     || アプリディ || ・アプリ内でのみ読み書きできる。        |
|| ファイル |           || レクトリ内 || ・センシティブな情報を扱うことができる。|
|           |           |             || ・ファイルは原則このタイプにするべき。  |
+-----------+-----------+-------------+------------------------------------------+
|| 読み取り || 読み取り || アプリディ || ・他アプリおよびユーザーも読み取り可能。|
|| 公開     |           || レクトリ内 || ・アプリ外部に公開（閲覧）可能な情報を  |
|| ファイル |           |             || 扱う。                                  |
+-----------+-----------+-------------+------------------------------------------+
|| 読み書き || 読み取り || アプリディ || ・他アプリおよびユーザーも読み書き可能。|
|| 公開     || 書き込み || レクトリ内 || ・セキュリティの観点からもアプリ設計の  |
|| ファイル |           |             || 観点からも使用は避けるべき。            |
+-----------+-----------+-------------+------------------------------------------+
|| 外部記憶 || 読み取り || SDカードな || ・アクセス権のコントロールができない。  |
|| ファイル || 書き込み || どの外部記 || ・他アプリやユーザーによるファイルの読み|
|| (読み書  |           || 憶装置     || 書き・削除が常に可能。                  |
|| き公開)  |           |             || ・使用は必要最小限にするべき。          |
|           |           |             || ・比較的容量の大きなファイルを扱うことが|
|           |           |             || できる。                                |
+-----------+-----------+-------------+------------------------------------------+
```
#### 非公開ファイルを扱う

同一アプリ内でのみ読み書きされるファイルを扱う場合であり、安全なファイルの使い方である。ファイルに格納する情報が公開可能かどうかに関わらず、できるだけファイルは非公開の状態で保持し、他アプリとの必要な情報のやり取りは別のAndroidの仕組み（Content
Provider、Service)を利用して行うことを原則とする。

ポイント：

1.  ファイルは、アプリディレクトリ内に作成する
2.  ファイルのアクセス権は、他のアプリが利用できないようにプライベートモードにする
3.  センシティブな情報を格納することができる
4.  ファイルに格納する(された)情報に対しては、その入手先に関わらず内容の安全性を確認する

PrivateFileActivity.java
```eval_rst
.. literalinclude:: CodeSamples/File PrivateFile.PrivateFileActivity.java
   :language: java
   :encoding: shift-jis
```


PrivateUserActivity.java
```eval_rst
.. literalinclude:: CodeSamples/File PrivateFile.PrivateUserActivity.java
   :language: java
   :encoding: shift-jis
```


#### 読み取り公開ファイルを扱う

不特定多数のアプリに対して内容を公開するためのファイルである。以下のポイントに気を付けて実装すれば、比較的安全なファイルの使い方になる。ただし、公開ファイルを作成するための、MODE\_WORLD\_READABLE変数はAPI
Level17以降ではdeprecatedとなっており、API Level 24
以降ではセキュリティ例外が発生するため、Content
Providerによるファイル共有方法が望ましい。

ポイント：

1.  ファイルは、アプリディレクトリ内に作成する
2.  ファイルのアクセス権は、他のアプリに対しては読み取り専用モードにする
3.  センシティブな情報は格納しない
4.  ファイルに格納する(された)情報に対しては、その入手先に関わらず内容の安全性を確認する

PublicFileActivity.java
```eval_rst
.. literalinclude:: CodeSamples/File PublicROFile.PublicFileActivity.java
   :language: java
   :encoding: shift-jis
```


PublicUserActivity.java
```eval_rst
.. literalinclude:: CodeSamples/File PublicROUser.PublicUserActivity.java
   :language: java
   :encoding: shift-jis
```


#### 読み書き公開ファイルを扱う

不特定多数のアプリに対して、読み書き権限を許可するファイルの使い方である。

不特定多数のアプリが読み書き可能ということは、マルウェアも当然内容の書き換えが可能であり、データの信頼性も安全性も全く保証されない。また、悪意のない場合でもファイル内のデータの形式や書き込みを行うタイミングなど制御が困難であり、そのようなファイルは機能面からも実用性が無いに等しい。

以上のように、セキュリティの観点からもアプリ設計の観点からも、読み書き公開ファイルを安全に運用することは不可能であり、読み書き公開ファイルの使用は避けなければならない。

ポイント：

1.  他アプリから読み書き可能なアクセス権を設定したファイルは作らない

#### 外部記憶(読み書き公開)ファイルを扱う

SDカードのような外部記憶デバイス上にファイルを格納する場合である。比較的容量の大きな情報を格納する(Webからダウンロードしたファイルを置くなどの)場合や外部に情報を持ち出す(バックアップなどの)場合に利用することが想定される。

「外部記憶(読み書き公開)ファイル」は不特定多数のアプリに対して「読み取り公開ファイル」と同等の性質を持つ。さらにandroid.permission.WRITE\_EXTERNAL\_STORAGE
Permissionを利用宣言している不特定多数のアプリに対しては「読み書き公開ファイル」と同等の性質を持つ。そのため、外部記憶(読み書き公開)ファイルの使用は必要最小限にとどめるべきである。

Androidアプリの慣例として、バックアップファイルは外部記憶デバイス上に作成されることが多い。しかし外部記憶デバイス上のファイルは前述のようにマルウェアを含む他のアプリから改ざんや削除されてしまうリスクがある。ゆえにバックアップを出力するアプリでは「バックアップファイルは速やかにPC等の安全な場所にコピーしてください」といった警告表示をするなど、アプリの仕様や設計面でのリスク最小化の工夫も必要となる。

ポイント：

1.  センシティブな情報は格納しない
2.  アプリ毎にユニークなディレクトリにファイルを配置する
3.  ファイルに格納する(された)情報に対しては、その入手先に関わらず内容の安全性を確認する
4.  利用側のアプリで書き込みを行わない仕様にする

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/File ExternalFile.app.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```


ExternalFileActivity.java
```eval_rst
.. literalinclude:: CodeSamples/File ExternalFile.ExternalFileActivity.java
   :language: java
   :encoding: shift-jis
```


利用側のサンプルコード

ExternalUserActivity.java
```eval_rst
.. literalinclude:: CodeSamples/File ExternalUser.ExternalUserActivity.java
   :language: java
   :encoding: shift-jis
```


AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/File ExternalUser.app.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```


### ルールブック<!-- 2b11bdc8 -->

ファイルを扱う場合には以下のルールを守ること。

1.  ファイルは原則非公開ファイルとして作成する （必須）
2.  他のアプリから読み書き権限でアクセス可能なファイルは作成しない （必須）
3.  SDカードなど外部記憶デバイスに格納するファイルの利用は必要最小限にする （必須）
4.  ファイルの生存期間を考慮してアプリの設計を行う （必須）

#### ファイルは原則非公開ファイルとして作成する （必須）

「4.6ファイルを扱う」「4.6.1.1非公開ファイルを扱う」で述べたように、格納する情報の内容に関わらずファイルは原則非公開にするべきである。Androidのセキュリティ設計の観点からも、情報のやり取りとそのアクセス制御はContent
ProviderやServiceなどのAndroidの仕組みの中で行うべきであり、できない事情がある場合のみファイルのアクセス権で代用することを検討することになる。

各ファイルタイプのサンプルコードや以下のルールの項も参照のこと。

#### 他のアプリから読み書き権限でアクセス可能なファイルは作成しない （必須）

「4.6.1.3読み書き公開ファイルを扱う」で述べたように、他のアプリに対してファイルの読み書きを許可すると、ファイルに格納される情報の制御ができない。そのため、セキュリティ的な観点からも機能・設計的な観点からも読み書き公開ファイルを利用した情報の共有を考えるべきではない。

#### SDカードなど外部記憶デバイスに格納するファイルの利用は必要最小限にする （必須）

「4.6.1.4外部記憶(読み書き公開)ファイルを扱う」で述べたように、SDカードをはじめとする外部記憶デバイスにファイルを置くことは、セキュリティおよび機能の両方の観点から潜在的な問題を抱えることに繋がる。一方で、SDカードはアプリディレクトリより生存期間の長いファイルを扱え、アプリ外部にデータを持ち出すのに常時使える唯一のストレージなので、アプリの仕様によっては使用せざるを得ないケースも多いと考えられる。

外部記憶デバイスにファイルを格納する場合、不特定多数のアプリおよびユーザーが読み・書き・削除できることを考慮して、サンプルコードで述べたポイントを含めて以下のようなポイントに気をつけてアプリの設計を行う必要がある。

-   原則としてセンシティブな情報は外部記憶デバイス上のファイルに保存しない

-   もしセンシティブな情報を外部記憶デバイス上のファイルに保存する場合は暗号化する

-   他アプリやユーザーに改ざんされては困る情報を外部記憶デバイス上のファイルに保存する場合は電子署名も一緒に保存する

-   外部記憶デバイス上のファイルを読み込む場合、読み込むデータの安全性を確認してからデータを利用する

-   他のアプリやユーザーによって外部記憶デバイス上のファイルはいつでも削除されることを想定してアプリを設計しなければならない

「4.6.2.4ファイルの生存期間を考慮してアプリの設計を行う
（必須）」も参照すること。

#### ファイルの生存期間を考慮してアプリの設計を行う （必須）

アプリディレクトリに保存されたデータは以下のユーザー操作により消去される。アプリの生存期間と一致する、またはアプリの生存期間より短いのが特徴である。

-   アプリのアンインストール

-   各アプリのデータおよびキャッシュの消去（「設定」→「アプリケーション」→「アプリケーションの管理」）

SDカード等の外部記憶デバイス上に保存されたファイルは、アプリの生存期間よりファイルの生存期間が長いことが特徴である。さらに次の状況も想定する必要がある。

-   ユーザーによるファイルの消去

-   SDカードの抜き取り・差し替え・アンマウント

-   マルウェアによるファイルの消去

このようにファイルの保存場所によってファイルの生存期間が異なるため、本節で説明したようなセンシティブな情報を保護する観点だけでなく、アプリとして正しい動作を実現する観点でもファイルの保存場所を正しく選択する必要がある。

### アドバンスト<!-- c8492450 -->

#### ファイルディスクリプタ経由のファイル共有

他のアプリに公開ファイルを直接アクセスさせるのではなく、ファイルディスクリプタ経由でファイル共有する方法がある。Content
ProviderとServiceでこの方法が使える。Content
ProviderやServiceの中で非公開ファイルをオープンし、そのファイルディスクリプタを相手のアプリに渡す。相手アプリはファイルディスクリプタ経由でファイルを読み書きできる。

他のアプリにファイルを直接アクセスさせるファイル共有方法とファイルディスクリプタ経由のファイル共有方法の比較を表
4.6‑2に示す。アクセス権のバリエーションとアクセス許可するアプリの範囲でメリットがある。特にアクセスを許可するアプリを細かく制御できるところがセキュリティ観点ではメリットが大きい。

表 4.6‑2　アプリ間ファイル共有方法の比較
```eval_rst
+-------------------------+--------------------------------+--------------------------------+
| ファイル共有方法        | アクセス権設定のバリエーション | アクセスを許可するアプリの範囲 |
+=========================+================================+================================+
|| 他のアプリにファイルを || 読み取り                      || すべてのアプリに対して        |
|| 直接アクセスさせる     || 書き込み                      || 一律アクセス許可して          |
|| ファイル共有           || 読み取り＋書き込み            || てしまう                      |
+-------------------------+--------------------------------+--------------------------------+
|| ファイルディスクリプタ || 読み取り                      || Content ProviderやService     |
|| 経由のファイル共有     || 書き込み                      || にアクセスしてくるアプリに    |
|                         || 追記のみ                      || 対して個別に一時的にアクセ    |
|                         || 読み取り＋書き込み            || ス許可・不許可を制御できる    |
|                         || 読み取り＋追記のみ            |                                |
+-------------------------+--------------------------------+--------------------------------+
```
上記ファイル共有方法のどちらにも共通することであるが、他のアプリにファイルの書き込みを許可するとファイル内容の完全性が保証しづらくなる。特に複数のアプリから同時に書き込みが行われると、ファイル内容のデータ構造が壊れてしまいアプリが正常に動作しなくなるリスクがある。他のアプリとのファイル共有においては、読み込み権限だけを許可するのが望ましい。

以下では、Content
Providerでファイルを共有する実装例(非公開Providerの場合)をサンプルコードとして掲載する。

ポイント

1.  利用元アプリは自社アプリであるから、センシティブな情報を保存してよい
2.  自社限定Content Providerアプリからの結果であっても、結果データの安全性を確認する

InhouseProvider.java
```eval_rst
.. literalinclude:: CodeSamples/File InhouseProvider.InhouseProvider.java
   :language: java
   :encoding: shift-jis
```


InhouseUserActivity.java
```eval_rst
.. literalinclude:: CodeSamples/File InhouseProviderUser.InhouseUserActivity.java
   :language: java
   :encoding: shift-jis
```


#### ディレクトリのアクセス権設定

これまでファイルに着目してセキュリティの考慮点を説明してきた。ファイルのコンテナであるディレクトリについてもセキュリティの考慮が必要である。ここではディレクトリのアクセス権設定についてセキュリティ上の考慮ポイントを説明する。

Androidには、アプリディレクトリ内にサブディレクトリを取得・作成するメソッドがいくつか用意されている。主なものを表
4.6‑3に示す。

表
4.6‑3 アプリディレクトリ配下のサブディレクトリ取得・作成メソッド
```eval_rst
====================================== ====================== ========================
 ..                                    | 他アプリに対する     | ユーザーによる削除
                                       | アクセス権の指定     |
====================================== ====================== ========================
Context\#getFilesDir()                 | 不可(実行権限のみ)   | 「設定」→「アプリ」→
                                                              | アプリケーションを選択
                                                              | →「データを消去」
Context\#getCacheDir()                 | 不可(実行権限のみ)   | 「設定」→「アプリ」→
                                                              | アプリケーションを選択
                                                              | →「キャッシュを消去」
                                                              | ※「データを消去」でも
                                                              | 削除される。
Context\#getDir(String name, int mode) | modeに以下を指定可能 | 「設定」→「アプリ」→
                                       | MODE_PRIVATE         | アプリケーションを選択
                                       | MODE_WORLD_READABLE  | →「データを消去」
                                       | MODE_WORLD_WRITEABLE
====================================== ====================== ========================
```
ここで特に気を付けるのはContext\#getDir()によるアクセス権の設定である。ファイルの作成でも説明しているように、Androidのセキュリティ設計の観点からディレクトリも基本的には非公開にするべきであり、アクセス権の設定によって情報の共有を行うと思わぬ副作用があるので、情報の共有には他の手段を考えるべきである。

##### MODE\_WORLD\_READABLE
```eval_rst
すべてのアプリに対してディレクトリの読み取り権限を与えるフラグである。すべてのアプリがディレクトリ内のファイル一覧や個々のファイルの属性情報を取得可能になる。このディレクトリ配下に秘密のファイルを配置することはできないため、通常はこのフラグを使用してはならない [18]_。

.. [18] MODE\_WORLD\_READABLEおよびMODE\_WORLD\_WRITEABLEは API Level17 以降ではdeprecated となっており、API Level 24 以降ではセキュリティ例外が発生するため使用できなくなっている。
```

##### MODE\_WORLD\_WRITEABLE
```eval_rst
他アプリに対してディレクトリの書き込み権限を与えるフラグである。すべてのアプリがディレクトリ内のファイルを作成、移動 [19]_、リネーム、削除が可能になる。これらの操作はファイル自体のアクセス権設定（読み取り、書き込み、実行）とは無関係であり、ディレクトリの書き込み権限があるだけで可能となる操作であることに注意が必要だ。他のアプリから勝手にファイルを削除されたり置き換えられたりするため、通常はこのフラグを使用してはならない [18]_。

.. [19] 内部ストレージから外部記憶装置(SDカードなど)への移動などマウントポイントを超えた移動はできない。そのため、読み取り権限のない内部ストレージファイルが外部記憶装置に移動されて読み書き可能になるようなことはない。
```
表 4.6‑3の「ユーザーによる削除」に関しては、「4.6.2.4ファイルの生存期間を考慮してアプリの設計を行う （必須）」を参照のこと。

#### Shared Preferenceやデータベースファイルのアクセス権設定

Shared
Preferenceやデータベースもファイルで構成される。アクセス権設定についてはファイルと同じことが言える。したがってShared
Preferenceもデータベースもファイルと同様に基本的には非公開ファイルとして作成し、内容の共有はAndroidのアプリ間連携の仕組みによって実現するべきである。

Shared
Preferenceの使用例を次に示す。MODE\_PRIVATEにより非公開ファイルとしてShared
Preferenceを作成している。

Shared Preferenceファイルにアクセス制限を設定する例
```java
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

// ～省略～

        // Shared Preferenceを取得する（なければ作成される）
        // ポイント：基本的にMODE_PRIVATEモードを指定する
        SharedPreferences preference = getSharedPreferences(
                PREFERENCE_FILE_NAME, MODE_PRIVATE);

        // 値が文字列のプリファレンスを書き込む例
        Editor editor = preference.edit();
        editor.putString("prep_key", "prep_value");// key:"prep_key", value:"prep_value"
        editor.commit();
```

データベースについては「4.5 SQLiteを使う」を参照すること。

#### Android 4.4 (API Level 19)における外部ストレージへのアクセスに関する仕様変更について

Android 4.4 (API Level
19)以降の端末において、外部ストレージへのアクセスに関して以下のように仕様が変更された。

(1) 外部ストレージ上のアプリ固有ディレクトリに読み書きする場合は、WRITE\_EXTERNAL\_STORAGE/READ\_EXTERNAL\_STORAGE Permissionが不要である(変更箇所)

(2) 外部ストレージ上のアプリ固有ディレクトリ以外の場所にあるファイルを読み込む場合は、READ\_EXTERNAL\_STORAGE Permissionが必要である(変更箇所)

(3) プライマリ外部ストレージ上のアプリ固有ディレクトリ以外の場所にファイルを書き込む場合は、WRITE\_EXTERNAL\_STORAGE Permissionが必要である

(4) セカンダリ以降の外部ストレージにはアプリ固有ディレクトリ以外の場所に書き込みは出来ない

この仕様では、Android
OSのバージョンによってPermissionの利用宣言の要・不要が変わっているため、Android
4.4 (API Level
19)をまたいで端末のサポートが必要なアプリの場合は、インストールする端末のバージョンによって不要なPermissionをユーザーに要求することになり、好ましい状況とは言えない。よって、上記仕様(1)のみに該当するアプリの場合は、\<uses-permission\>タグのmaxSdkVersion属性を以下のように記述して対応することをお薦めする。

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/File ExternalFile.app.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```


#### Android 7.0（API Level 24）における外部ストレージの特定ディレクトリへのアクセスに関する仕様変更について

Android 7.0（API Level
24）以降の端末において、外部ストレージの特定ディレクトリに対し、Permissionの利用宣言なしにアクセスできるための仕組みとして、「Scoped
Directory Access」が導入された。

Scoped Directory
Accessでは、StorageVolume\#createAccessIntentメソッドの引数にEnviromentクラスで定義されたディレクトリを指定し、Intentを生成する。生成されたIntentをstartActivityForResultで送信することで、画面上にアクセスの許可を求めるダイアログが表示され、ユーザーが許可をするとストレージボリュームごとの指定されたディレクトリへアクセスが可能となる。

表　4.6-4 Scoped Directory Accessによってアクセスできるディレクトリ
```eval_rst
========================== ========================================================
DIRECTORY\_MUSIC           一般的な音楽ファイルの標準ディレクトリ
DIRECTORY\_PODCASTS        ポッドキャストの標準ディレクトリ
DIRECTORY\_RINGTONES       着信音の標準ディレクトリ
DIRECTORY\_ALARMS          アラーム音の標準ディレクトリ
DIRECTORY\_NOTIFICATIONS   通知音の標準ディレクトリ
DIRECTORY\_PICTURES        画像ファイルの標準ディレクトリ
DIRECTORY\_MOVIES          動画ファイルの標準ディレクトリ
DIRECTORY\_DOWNLOADS       ユーザーがダウンロードしたファイルの標準ディレクトリ
DIRECTORY\_DCIM            カメラによる画像・動画ファイルの標準ディレクトリ
DIRECTORY\_DOCUMENTS       ユーザーによって作られたドキュメントの標準ディレクトリ
========================== ========================================================
```
アプリがアクセスする必要のある領域が上記のディレクトリであるならば、Android
7.0以上の端末で動作させる場合は、以下の理由によりScoped Directory
Access機能を利用することを推奨する。Android7.0をまたいで端末のサポートが必要なアプリの場合は、「4.6.3.4
Android 4.4 (API Level
19)における外部ストレージへのアクセスに関する仕様変更について」で掲載したAndroidManifestの記述例を参照すること。

-   外部ストレージにアクセスできるPermissionを付与した場合、アプリの目的外のディレクトリにもアクセスできてしまう。

-   Storage Access
    Frameworkでアクセスできるディレクトリをユーザーに選択させる場合、その都度ピッカー上で煩雑な操作を求められる。また、外部ストレージのルートディレクトリにアクセス許可を与えた場合は、外部ストレージ全体にアクセスできる。

Browsable Intentを利用する
--------------------------

ブラウザからWebページのリンクに対応して起動するようにアプリを作ることができる。Browsable
Intentという機能である。アプリは、URIスキームをManifestファイルで指定することで、そのURIスキームを持つリンクへの移動(ユーザーのタップなど)に反応し、リンクをパラメータとして起動することが可能になる。

また、URIスキームを利用することでブラウザから対応するアプリを起動する方法は、AndroidのみならずiOS他のプラットフォームでも対応しており、Webアプリとの外部アプリ連携などに一般的に使われている。例えば、TwitterアプリやFacebookアプリでは次のようなURIスキームが定義されており、AndroidでもiOSでもブラウザから対応するアプリが起動するようになっている。

表 4.7‑1
```eval_rst
============= ================
URIスキーム   対応するアプリ
============= ================
fb://         Facebook
twitter://    Twitter
============= ================
```
このように連携や利便性を考えた便利な機能であるが、悪意ある第三者に悪用される危険性も潜んでいる。悪意のあるWebサイトを用意してリンクのURLに不正なパラメータを仕込むことでアプリの機能を悪用したり、同じURIスキームに対応したマルウェアをインストールさせてURLに含まれる情報を横取りしたりするなどが考えられる。

このような危険性に対応するために、利用する際にはいくつかのポイントに気をつけなければならない。

### サンプルコード<!-- bde3d2b5 -->

以下に、Browsable Intentを利用したアプリのサンプルコードを示す。

ポイント：

1.  (Webページ側)対応するURIスキーマを使ったリンクのパラメータにセンシティブな情報を含めない
2.  URLのパラメータを利用する前に値の安全性を確認する

Starter.html
```html
<html>
    <body>
<!-- ★ポイント1★ URLにセンシティブな情報を含めない -->
<!-- URLパラメータとして渡す文字列は、UTF-8で、かつURIエンコードしておくこと -->
        <a href="secure://jssec?user=user_id"> Login </a>
    </body>
</html>
```

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Browsable Intent.app.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```


BrowsableIntentActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Browsable Intent.BrowsableIntentActivity.java
   :language: java
   :encoding: shift-jis
```


### ルールブック<!-- f1ed81a9 -->

Browsable Intentを利用する場合には以下のルールを守ること。

1.  （Webページ側）対応するリンクのパラメータにセンシティブな情報を含めない （必須）
2.  URLのパラメータを利用する前に値の安全性を確認する （必須）

#### （Webページ側）対応するリンクのパラメータにセンシティブな情報を含めない （必須）

ブラウザ上でリンクをタップした際、data（Intent\#getDataにて取得)にURLの値が入ったIntentが発行され、システムにより該当するIntent
Filterを持つアプリが起動する。

この時、同じURIスキームを受け付けるようIntent
Filterが設定されたアプリが複数存在する場合は、通常の暗黙的Intentによる起動と同様にアプリ選択のダイアログが表示され、ユーザーの選択したアプリが起動することになる。仮に、アプリ選択画面の選択肢としてマルウェアが存在していた場合は、ユーザーが誤ってマルウェアを起動させてしまう危険性があり、パラメータがマルウェアに渡ることになる。

このようにWebページのリンクURLに含めたパラメータはすべてマルウェアに渡る可能性があるので、一般のWebページのリンクを作るときと同様に、URLのパラメータに直接センシティブな情報を含めることは避けなければならない。

URLにユーザーIDとパスワードが入っている例
```
insecure://sample/login?userID=12345&password=abcdef
```

また、URLのパラメータがユーザーIDなどセンシティブでない情報のみの場合でも、アプリ起動時のパスワード入力をアプリ側でさせるような仕様では、ユーザーが気付かずにマルウェアを起動してしまい、マルウェアに対してパスワードを入力してしまう危険性もある。そのため、一連のログイン処理自体はアプリ側で完結するような仕様を検討すべきである。Browsable
Intentによるアプリ起動はあくまで暗黙的Intentによるアプリ起動であり、意図したアプリが起動される保証がないことを念頭に置いたアプリ・サービス設計を心がける必要がある。

#### URLのパラメータを利用する前に値の安全性を確認する （必須）

URIスキーマに合わせたリンクは、アプリ開発者に限らず誰でも作成可能なので、アプリに渡されたURLのパラメータが正規のWebページから送られてくるとは限らない。また、渡されたURLのパラメータが正規のWebページから送られてきたかどうかを調べる方法もない。

そのため、渡されたURLのパラメータを利用する前に、パラメータに想定しない値が入っていないかなど、値の安全性を確認する必要がある。

LogCatにログ出力する
--------------------
```eval_rst
AndroidはLogCatと呼ばれるシステムログ機構があり、システムのログ情報だけでなくアプリのログ情報もLogCatに出力される。LogCatのログ情報は同じ端末内の他のアプリからも読み取り可能 [20]_ であるため、センシティブな情報をLogCatにログ出力してしまうアプリには情報漏洩の脆弱性があるとされる。LogCatにはセンシティブな情報をログ出力すべきではない。

.. [20] LogCat に出力されたログ情報は、READ\_LOGS Permissionを利用宣言したアプリであれば読み取り可能である。ただしAndroid
    4.1 以降ではLogCatに出力された他のアプリのログ情報は読み取り不可となった。また、スマートフォンユーザーであれば、ADB
    経由でLogCat のログ情報を参照することも可能である。
```
セキュリティ観点ではリリース版アプリでは一切ログ出力しないことが望ましい。しかし様々な理由によりリリース版アプリでもログ出力するケースがある。ここではリリース版アプリにおいてもログ出力しつつ、センシティブな情報はログ出力しない方法を紹介する。また「4.8.3.1
リリース版アプリにおけるログ出力の2つの考え方」も参照すること。

### サンプルコード<!-- a0d9430e -->

ここではProGuardを利用してリリース版アプリでのLogCatへのログ出力を制御する方法を紹介する。ProGuard
は使用されていないメソッド等、実質的に不要なコードを自動削除する最適化ツールの一つである。

Androidのandroid.util.Logクラスには5種類のログ出力メソッドLog.e()、Log.w()、Log.i()、Log.d()、Log.v()がある。ログ情報は、リリース版アプリで出力することを意図したログ情報（以下、運用ログ情報と呼ぶ）と、リリース版アプリで出力してはならない（たとえばデバッグ用の）ログ情報（以下、開発ログ情報と呼ぶ）を区別するべきである。運用ログ出力のためにはLog.e()/w()/i()を使用し、開発ログ出力のためにはLog.d()/v()を使用するとよい。5種類のログ出力メソッドの使い分けの詳細については「4.8.3.2
ログレベルとログ出力メソッドの選択基準」を参照すること。また、「4.8.3.3
DEBUGログとVERBOSEログは自動的に削除されるわけではない」も参照すること。

次ページ以降で、Log.d()/v()で出力する開発ログ情報を開発版アプリではログ出力し、リリース版アプリではログ出力しないサンプルコードを紹介する。このサンプルコードではLog.d()/v()呼び出しコードを自動削除するために、ProGuardを使用している。

ポイント：

1.  センシティブな情報はLog.e()/w()/i()、System.out/errで出力しない
2.  センシティブな情報をログ出力する場合はLog.d()/v()で出力する
3.  Log.d()/v()の呼び出しでは戻り値を使用しない(代入や比較)
4.  リリースビルドではLog.d()/v()の呼び出しが自動削除される仕組みを導入する
5.  リリース版アプリのAPKファイルはリリースビルドで作成する

ProGuardActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Log ProGuard.ProGuardActivity.java
   :language: java
   :encoding: shift-jis
```


proguard-project.txt
```shell
# クラス名、メソッド名等の変更を防ぐ
-dontobfuscate

# ★ポイント4★ リリースビルドではLog.d()/v()の呼び出しが自動削除される仕組みを導入する
-assumenosideeffects class android.util.Log {
    public static int d(...);
    public static int v(...);
}
```

★ポイント5★ リリース版アプリのAPKファイルはリリースビルドで作成する

![](media/image48.png)
```eval_rst
.. {width="7.26875in" height="2.7849146981627295in"}
```

図 4.8‑1リリース版アプリを作成する方法(Exportする)

開発版アプリ（デバッグビルド）とリリース版アプリ（リリースビルド）のLogCat出力の違いを図
4.8‑2に示す。

![](media/image49.png)
```eval_rst
.. {width="6.889763779527559in"
.. height="2.2236220472440946in"}
```

図 4.8‑2
Logメソッドの開発版アプリとリリース版アプリのLogCat出力の違い

### ルールブック<!-- 7121bb68 -->

LogCatにログを出力する際は、以下のルールを守ること。

1.  運用ログ情報にセンシティブな情報を含めない （必須）

2.  開発ログ情報を出力するコードをリリースビルド時に自動削除する仕組みを導入する （推奨）

3.  Throwableオブジェクトをログ出力するときはLog.d()/v()メソッドを使う （推奨）

4.  ログ出力にはandroid.util.Logクラスのメソッドのみ使用する （推奨）

#### 運用ログ情報にセンシティブな情報を含めない （必須）

LogCatに出力したログは他のアプリから読むことができるので、リリース版アプリがユーザーのログイン情報などのセンシティブな情報をログ出力することがあってはならない。開発中にセンシティブな情報をログ出力するコードを書かないようにするか、あるいは、リリース前にそのようなコードを全て削除することが必要である。

このルールを順守するためには、運用ログ情報にセンシティブな情報を含めないこと。さらに、センシティブな情報を出力するコードをリリースビルド時に削除する仕組みを導入することを強く推奨する。「4.8.2.2
開発ログ情報を出力するコードをリリースビルド時に自動削除する仕組みを導入する
（推奨）」を参照すること。

#### 開発ログ情報を出力するコードをリリースビルド時に自動削除する仕組みを導入する （推奨）

アプリ開発中は、複雑なロジックの処理過程の中間的な演算結果、プログラム内部の状態情報、通信プロトコルの通信データ構造など、処理内容の確認やデバッグ用でセンシティブな情報をログ出力させたいことがある。アプリ開発時にセンシティブな情報をデバッグログとして出力するのは構わないが、この場合は、「4.8.2.1
運用ログ情報にセンシティブな情報を含めない
（必須）」で述べたように、リリース前に必ず該当するログ出力コードを削除すること。

リリースビルド時に開発ログ情報を出力するコードを確実に削除するために、何らかのツールを用いてコード削除を自動化する仕組みを導入すべきである。そのためのツールに4.8.1で紹介したProGuardがある。以下では、ProGuardを使ったコード削除の仕組みを導入する際の注意を説明する。ここでは、「4.8.3.2
ログレベルとログ出力メソッドの選択基準」に準拠し、開発ログ情報をLog.d()/v()のいずれかのみで出力しているアプリに対して仕組みを適用することを想定している。

ProGuardは使用されていないメソッド等、実質的に不要なコードを自動削除する。Log.d()/v()を-assumenosideeffectsオプションの引数に指定することにより、Log.d()、Log.v()の呼び出しが実質的に不要なコードとみなされ、自動削除される。

Log.d()/v()を-assumenosideeffectsと指定することで、自動削除の対象にする
```shell
-assumenosideeffects class android.util.Log {
    public static int d(...);
    public static int v(...);
}
```

この自動削除の仕組みを利用する場合は、Log.v(),
Log.d()の戻り値を使用してしまうとLog.v()/d()のコードが削除されない点に注意が必要である。よって、Log.v(),
Log.d()の戻り値を使用してはならない。たとえば、次の実験コードにおいては、Log.v()が削除されない。

削除指定したLog.v()が削除されない実験コード削除指定したLog.v()が削除されない実験コード
```java
int i = android.util.Log.v("tag", "message");
System.out.println(String.format("Log.v()が%dを返した。", i));  // 実験のためLog.v()の戻り値を使用。
```

また、上記ProGuard設定により、Log.d()及びLog.v()が自動削除されることを前提としたソースコードがあったとする。もしそのソースコードをProGuard設定がされていない他のプロジェクトで再利用してしまうと、Log.d()及びLog.v()が削除されないため、センシティブな情報が漏洩してしまう危険性がある。ソースコードを再利用する際は、ProGuard設定を含めたプロジェクト環境の整合性を確保すること。

#### Throwableオブジェクトをログ出力するときはLog.d()/v()メソッドを使う （推奨）

「4.8.1 サンプルコード」および「4.8.3.2
ログレベルとログ出力メソッドの選択基準」に示した通り、Log.e()/w()/i()ではセンシティブな情報をログ出力してはならない。一方で、開発者がプログラムの異常を詳細にログ出力するために、例外発生時にLog.e(...,
Throwable tr)/w(..., Throwable tr)/i(..., Throwable
tr)でスタックトレースをLogCatにログ出力しているケースがみられる。しかしながら、スタックトレースはプログラムの内部構造を詳細に出力してしまうので、アプリケーションによってはセンシティブな情報が含まれてしまう場合がある。例えば、SQLiteExceptionをそのまま出力してしまうと、どのようなSQLステートメントが発行されたかが明らかになるので、SQLインジェクション攻撃の手がかりを与えてしまうことがある。よって、Throwableオブジェクトをログ出力する際には、Log.d()/Log.v()メソッドのみを使用することを推奨する。

#### ログ出力にはandroid.util.Logクラスのメソッドのみ使用する （推奨）

開発中にアプリが想定通りに動作していることを確認するために、System.out/errでログを出力することがあるだろう。もちろんSystem.out/errのprint()/println()メソッドでもLogCatにログを出力することは可能だが、以下の理由からログ出力にはandroid.util.Logクラスのメソッドのみを使用することを強く推奨する。

ログを出力するときは、一般には情報の緊急度に応じて出力メソッドを使い分け、出力を制御する。たとえば、深刻なエラー、警告、単なるアプリ情報通知などの区分が使われる。この区分をSystem.out/errに適用する手段の一つには、エラーと警告はSystem.err、それ以外はSystem.outで出力する方法がある。しかし、この場合、リリース時にも出力する必要のあるセンシティブでない情報(運用ログ情報)とセンシティブな情報が含まれている可能性のある情報(開発ログ情報)が同じメソッドによって出力されてしまう。よって、センシティブな情報を出力するコードを削除する際に、削除漏れが発生するおそれがある。

また、ログ出力にandroid.util.LogとSystem.out/errを使う場合は、android.util.Logのみを使う場合と比べて、ログ出力コードを削除する際に考慮することが増えるため、削除漏れなどのミスが生じるおそれがある。

上記のようなミスが生じる危険を減らすために、android.util.Logクラスのメソッドのみ使用することを推奨する。

### アドバンスト<!-- 309f2fdf -->

#### リリース版アプリにおけるログ出力の2つの考え方

リリース版Androidアプリにおけるログ出力の考え方には大きく分けて、一切ログ出力すべきではないという考え方と、後の解析のために必要な情報をログ出力すべきという考え方の2つがある。セキュリティ観点ではリリース版アプリでは一切ログ出力しないことが望ましい。しかし様々な理由によりリリース版アプリでもログ出力するケースがある。ここでは両者のそれぞれの考え方について述べる。

1つ目は、リリース版アプリにおいてログ出力することにはあまり価値がなく、しかもセンシティブな情報を漏洩してしまうリスクがあるので、「一切ログ出力すべきではない」という考え方である。この考え方は、多くのWeb
アプリ運用環境などと違い、Androidアプリ運用環境ではリリース後のアプリのログ情報を開発者が収集する手段が用意されていないことによるものである。この考え方に基づくと、開発中に使用したログ出力コードを最終版のソースコードから削除してリリース版アプリを作成するという運用がなされる。

2つ目は、カスタマーサポート等でアプリの不具合解析を行う最終手段として、「後の解析のために必要な情報をログ出力すべき」という考え方である。この考え方に基づくと、リリース版アプリではセンシティブな情報を誤ってログ出力してしまわないよう細心の注意が必要となるため、サンプルコードセクションで紹介したような人為的ミスを排除する運用が必要となる。なお、下記のGoogleのCode
Style Guidelineも2つ目の考え方に基づいている。

Code Style Guidelines for Contributors / Log Sparingly

[http://source.android.com/source/code-style.html\#log-sparingly](http://source.android.com/source/code-style.html#log-sparingly)

#### ログレベルとログ出力メソッドの選択基準

Androidのandroid.util.LogクラスにはERROR、WARN、INFO、DEBUG,VERBOSEの5段階のログレベルが定義されている。出力したいログ情報のログレベルに応じて、適切なandroid.util.Logクラスのログ出力メソッドを選択する必要がある。選択基準を表
4.8‑1にまとめた。

表 4.8‑1 ログレベルとログ出力メソッドの選択基準
```eval_rst
+------------+----------+--------------------------------+----------------------------------+
| ログレベル | メソッド | 出力するログ情報の趣旨         | アプリリリース時の注意           |
+============+==========+================================+==================================+
| ERROR      | Log.e()  | | アプリが致命的な状況         | | 左記のログ情報はユーザーも参   |
|            |          | | に陥ったときに出力す         | | 照することが想定される情報であ |
|            |          | | るログ情報。                 | | るため、開発版アプリとリリース |
|            |          |                                | | 版アプリの両方でログ出力される |
|            |          |                                | | べき情報である。そのためこのロ |
|            |          |                                | | グレベルではセンシティブな情報 |
|            |          |                                | | をログ出力してはならない。     |
+------------+----------+--------------------------------+                                  |
| WARN       | Log.w()  | | アプリが深刻な予期せ         |                                  |
|            |          | | ぬ状況に遭遇したとき         |                                  |
|            |          | | に出力するログ情報。         |                                  |
+------------+----------+--------------------------------+                                  |
| INFO       | Log.i()  | | 上記以外で、アプリの         |                                  |
|            |          | | 注目すべき状態の変化         |                                  |
|            |          | | や結果を知らせる目的         |                                  |
|            |          | | で出力するログ情報。         |                                  |
+------------+----------+--------------------------------+----------------------------------+
| DEBUG      | Log.d()  | | アプリ開発時に特定の         | | 左記のログ情報はアプリ開発者   |
|            |          | | バグの原因究明のため         | | 専用の情報であるため、リリース |
|            |          | | に一時的にログ出力し         | | 版アプリではログ出力されてはな |
|            |          | | たいプログラム内部の         | | らない情報である。開発版アプリ |
|            |          | | 状態情報。                   | | ではセンシティブな情報を出力し |
|            |          |                                | | ても構わないが、リリース版アプ |
|            |          |                                | | リでは絶対にセンシティブな情報 |
|            |          |                                | | をログ出力してはならない。     |
+------------+----------+--------------------------------+                                  |
| VERBOSE    | Log.v()  | | 以上のいずれにも該当         |                                  |
|            |          | | しないログ情報。アプ         |                                  |
|            |          | | リ開発者がさまざまな         |                                  |
|            |          | | 目的で出力するログ情         |                                  |
|            |          | | 報が該当する。               |                                  |
|            |          | | サーバーとの通信デー         |                                  |
|            |          | | タをダンプ出力したい         |                                  |
|            |          | | 場合など。                   |                                  |
+------------+----------+--------------------------------+----------------------------------+
```
より詳細なログ出力の作法については下記URLを参照すること。

Code Style Guidelines for Contributors / Log Sparingly

[http://source.android.com/source/code-style.html\#log-sparingly](http://source.android.com/source/code-style.html#log-sparingly)

#### DEBUGログとVERBOSEログは自動的に削除されるわけではない
```eval_rst
Developer Referenceのandroid.util.Logクラスの解説 [21]_ には次のような記載がある。

The order in terms of verbosity, from least to most is ERROR, WARN,
INFO, DEBUG, VERBOSE. Verbose should never be compiled into an
application except during development. Debug logs are compiled in but
stripped at runtime. Error, warning and info logs are always kept.

.. [21] http://developer.android.com/intl/ja/reference/android/util/Log.html
```
開発者の中には、この文章からLogクラスの動作を次のように誤った解釈をしている人がいる。

-   Log.v()呼び出しはリリースビルド時にはコンパイルされず、VERBOSEログが出力されることがなくなる

-   Log.d()呼び出しはコンパイルされるが、実行時にはDEBUGログが出力されることはない

しかし実際にはLog
クラスはこのようには動作せず、デバッグビルド、リリースビルドを問わず全てのログを出力してしまう。よく読んでみるとわかるが、この英文はLogクラスの動作について語っているのではなく、ログ情報とはこうあるべきということを説明しているだけである。

この記事のサンプルコードでは、ProGuardを使って上記英文のような動作を実現する方法を紹介している。

#### ログ情報の組み立て処理を削除する

下記ソースコードをProGuardでリリースビルドしてLog.d()を削除した場合、Log.d()の呼び出し処理（下記コードの2行目）は削除されるものの、その前段でセンシティブな情報を組み立てる処理（下記コードの1行目）は削除されないことに注意が必要である。

```java
    String debug_info = String.format("%s:%s", "センシティブな情報1", "センシティブな情報2");
    if (BuildConfig.DEBUG) android.util.Log.d(TAG, debug_info);
```
上記ソースコードをリリースビルドしたAPKファイルを逆アセンブルすると次のようになる。確かにLog.d()の呼び出し処理は存在しないが、"センシティブな情報1"といった文字列定数定義とString\#format()メソッドの呼び出し処理が削除されず残っていることが分かる。

```
    const-string v1, "%s:%s"
    const/4 v2, 0x2
    new-array v2, v2, [Ljava/lang/Object;
    const/4 v3, 0x0
    const-string v4, "センシティブな情報1"
    aput-object v4, v2, v3
    const/4 v3, 0x1
    const-string v4, "センシティブな情報2"
    aput-object v4, v2, v3
    invoke-static {v1, v2}, Ljava/lang/String;->format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    move-result-object v0
```

実際にはAPKファイルを逆アセンブルして、上記のようにログ出力情報を組み立てている箇所を発見するのは容易なことではない。しかし非常に機密度の高い情報を扱っているアプリにおいては、このような処理がAPKファイルに残ってしまってはならない場合もあり得る。
```eval_rst
もし上記のようなログ出力情報の組み立て処理も削除してしまいたい場合には、次のように記述するとよい [22]_。リリースビルド時にはコンパイラの最適化処理によって、下記サンプルコードの処理は丸ごと削除される。

.. [22] 前述のサンプルコードを、条件式にBuildConfig.DEBUGを用いたif文で囲った。Log.d()呼び出し前のif文は不要であるが、前述のサンプルコードと対比させるため、そのまま残した。
```
```java
    if (BuildConfig.DEBUG) { 
        String debug_info = String.format("%s:%s", "センシティブな情報1", "センシティブな情報2");
        if (BuildConfig.DEBUG) android.util.Log.d(TAG, debug_info);
    }
```

なお、下記ソースコードにProGuardを適用した場合も、同様にログ情報の組み立て処理（"result:"+ valueの部分）が残ってしまう。

```java
    Log.d(TAG, "result:" + value);
```

この場合も下記のように対処すればよい。

```java
    if (BuildConfig.DEBUG) Log.d(TAG, "result:" + value);
```

#### Intentの内容がLogCatに出力される

Activityを利用する際にActivityManagerがIntentの内容をLogCatに出力するため、注意が必要である。「4.1.3.5
Activity利用時のログ出力について」を参照すること。

#### System.out/errに出力されるログの抑制

System.out/errの出力先はLogCatである。System.out/errに出力されるのは、開発者がデバッグのために出力したログに限らない。例えば、次の場合、スタックトレースはSystem.errに出力される。

-   Exception\#printStackTrace()を使った場合

-   暗黙的にSystem.errに出力される場合\
（例外をアプリでキャッチしていない場合、システムがException\#printStackTrace()に渡すため。）

スタックトレースにはアプリ固有の情報が含まれるため、例外は開発者が正しくハンドリングすべきである。

保険的対策として、System.out/errの出力先をLogCat以外に変更する方法がある。以下に、リリースビルド時にSystem.out/errの出力先を変更し、どこにもログ出力しないようにする実装例を挙げる。ただし、この対応はSystem.out/errの出力先をアプリの実行時に一時的に書き換えるので、アプリやシステムの誤動作に繋がらないかどうかを充分に検討する必要がある。また、この対策はアプリ自身のプロセスには有効であるが、システムプロセスが生成するエラーログを抑制することはできない。すべてのエラーを抑制できるわけではないことに注意すること。

OutputRedirectApplication.java
```eval_rst
.. literalinclude:: CodeSamples/Log OutputRedirection.OutputRedirectApplication.java
   :language: java
   :encoding: shift-jis
```


AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/Log OutputRedirection.app.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```


proguard-project.txt
```shell
# クラス名、メソッド名等の変更を防ぐ
-dontobfuscate

# リリースビルド時にLog.d()/v()の呼び出しを自動的に削除する
-assumenosideeffects class android.util.Log {
    public static int d(...);
    public static int v(...);
}

# リリースビルド時にresetStreams()を自動的に削除する
-assumenosideeffects class org.jssec.android.log.outputredirection.OutputRedirectApplication {
    private void resetStreams(...);
}
```

開発版アプリ（デバッグビルド）とリリース版アプリ（リリースビルド）のLogCat出力の違いを図
4.8‑3に示す。

![](media/image50.png)
```eval_rst
.. {width="7.26875in" height="2.237303149606299in"}
```

図 4.8‑3
System.out/errの開発版アプリとリリース版アプリのLogCat出力の違い

WebViewを使う
-------------

WebサイトやHTMLファイルを閲覧する機能を実装する方法として、WebViewを使用することができる。WebViewはHTMLをレンダリングする、JavaScriptを実行するなど、この目的のために有用な機能を提供する。

### サンプルコード<!-- 1363357c -->

WebViewを使用することにより容易にWebサイト、HTMLファイル閲覧機能を実現することができるが、アクセスするコンテンツの特性によってWebViewが抱えるリスクや適切な防衛手段が異なってくる。

特に気をつけなければいけないのはJavaScriptの使用である。WebViewのデフォルト設定ではJavaScriptの機能が無効になっているが、WebSettings\#setJavaScriptEnabled()メソッドにより有効にすることが可能である。JavaScriptを使用することでインタラクティブなコンテンツの表示が可能になるが、悪意のある第三者により端末の情報を取得される、あるいは端末を操作されるという被害が発生する可能性がある。
```eval_rst
WebViewを用いてコンテンツにアクセスするアプリを開発する際は、次の原則に従うこと [23]_。

(1) 自社が管理しているコンテンツにのみアクセスする場合に限りJavaScriptを有効にしてよい

(2) 上記以外の場合には、JavaScriptを有効にしてはならない

.. [23] 厳密に言えば安全性を保証できるコンテンツであればJavaScriptを有効にしてよい。自社管理のコンテンツであれば自社の努力で安全性を確保できるし責任も取れる。では信頼できる提携会社のコンテンツは安全だろうか？これは会社間の信頼関係により決まる。信頼できる提携会社のコンテンツを安全であると信頼してJavaScriptを有効にしてもよいが、万一の場合は自社責任も伴うため、ビジネス責任者の判断が必要となる。
```
開発しているアプリがアクセスするコンテンツの特性を踏まえ、図 4.9‑1に従いサンプルコードを選択することが必要である。

![](media/image51.png)
```eval_rst
.. {width="6.889763779527559in"
.. height="2.912204724409449in"}
```

図 4.9‑1
WebViewのサンプルコードを選択するフローチャート

#### assetsまたはresディレクトリに配置したコンテンツのみを表示する

端末内のローカルコンテンツをWebViewで表示するアプリに関しては、アプリのAPKに含まれるassetsあるいはresディレクトリ内のコンテンツにアクセスする場合に限りJavaScriptを有効にしてもよい。

以下にWebViewを使用してassetsディレクトリ内にあるHTMLファイルを表示するサンプルコードを示す。

ポイント：

1.  assetsとresディレクトリ以外の場所に配置したファイルへのアクセスを禁止にする
2.  JavaScriptを有効にしてよい

WebViewAssetsActivity.java
```eval_rst
.. literalinclude:: CodeSamples/WebView Assets.WebViewAssetsActivity.java
   :language: java
   :encoding: shift-jis
```


#### インターネット上の自社管理コンテンツを表示する

自社の管理するサービス上のコンテンツを表示する場合、サービス側、アプリ側の双方で適切な対策を施し、安全が確保できるならば、JavaScriptを有効にしてもよい。

-   サービス側の対策

図 4.9‑2に示したように、サービス側に用意するコンテンツは自社の管理していないコンテンツを参照してはならない。加えて、サービスに適切なセキュリティ対策が施されていることも必要である。その理由は、サービスを構成するコンテンツへの攻撃コードの埋め込みや改ざんを防止することにある。「4.9.2.1 JavaScriptを有効にするのはコンテンツを自社が管理している場合に限定する （必須）」を参照すること。

-   アプリ側の対策

次にアプリ側での対策を述べる。アプリ側では、接続先が自社管理サービスであることを確認することが必要である。そのために、通信プロトコルはHTTPSを使用し、証明書が信頼できる場合のみ接続するように実装する。

以下では、アプリ側での実装の例として、WebViewを使って自社管理コンテンツを表示するActivityの例を示す。

![](media/image52.png)
```eval_rst
.. {width="6.471653543307086in"
.. height="4.366141732283465in"}
```

図 4.9‑2アプリが読み込んでよい自社管理コンテンツ

ポイント：

1.  WebViewのSSL通信エラーを適切にハンドリングする
2.  WebViewのJavaScriptを有効にしてもよい
3.  WebViewで表示するURLをHTTPSプロトコルだけに限定する
4.  WebViewで表示するURLを自社管理コンテンツだけに限定する

WebViewTrustedContentsActivity.java
```eval_rst
.. literalinclude:: CodeSamples/WebView TrustedContents.WebViewTrustedContentsActivity.java
   :language: java
   :encoding: shift-jis
```


#### 自社管理以外のコンテンツを表示する

自社で管理していないコンテンツをWebViewで接続・表示する場合は、JavaScriptを有効にしてはならない。攻撃者が用意したコンテンツに接続する可能性があるからである。

以下のサンプルコードはWebViewを使用して自社管理以外のコンテンツを表示するアプリである。このアプリは、アドレスバーに入力したURLの指すHTMLファイルなどのコンテンツを読み込み、画面に表示する。安全の確保のためにJavaScriptを無効化しているほか、HTTPSで通信していてSSLエラーが発生した場合は接続を中止する実装となっている。SSLエラーは「4.9.1.2
インターネット上の自社管理コンテンツを表示する」と同様の方法によりハンドリングしている。HTTPS通信についての詳細は、「5.4
HTTPSで通信する」を参照すること。

ポイント：

1.  HTTPS 通信の場合にはSSL通信のエラーを適切にハンドリングする
2.  JavaScriptを有効にしない

WebViewUntrustActivity.java
```eval_rst
.. literalinclude:: CodeSamples/WebView Untrust.WebViewUntrustActivity.java
   :language: java
   :encoding: shift-jis
```


### ルールブック<!-- c6c109b5 -->

WebViewを使用する際には以下のルールを守ること。

1.  JavaScriptを有効にするのはコンテンツを自社が管理している場合に限定する （必須）

2.  自社管理サービスとの通信にはHTTPSを使用する （必須）

3.  Intent経由など、他から受け取ったURLはJavaScriptが有効なWebViewには表示しない （必須）

4.  SSL通信のエラーを適切にハンドリングする （必須）

#### JavaScriptを有効にするのはコンテンツを自社が管理している場合に限定する （必須）

WebViewを用いてコンテンツやサービスにアクセスするアプリを開発する際に、セキュリティの面で最も注意しなければならない点はJavaScriptを有効にするかどうかである。原則的には、自社が管理しているサービスにのみアプリがアクセスする場合に限りJavaScriptを有効にしてもよい。しかし、そうでないサービスにアクセスする可能性が少しでもある場合には、JavaScriptを有効にしてはならない。

##### 自社で管理しているサービス

自社で作成あるいは、運用、管理に責任を持つサービスは、自社が安全を保証できる。例として、自社管理サーバー上の自社開発コンテンツにアプリがアクセスする場合を考える。各コンテンツがサーバー内部のコンテンツのみを参照しており、かつ、自社管理サーバーに対して適切なセキュリティ対策が施されているならば、このサービスは自社以外が内容を書き換えていることはないとみなせる。この場合、自社管理サービスにアクセスするアプリのJavaScriptを有効にしてもよい。「4.9.1.2
インターネット上の自社管理コンテンツを表示する」を参照すること。また、他のアプリによる書き換えが不可能な端末内コンテンツ(APKのassetsまたはresディレクトリ内に配置されたコンテンツやアプリディレクトリ下のコンテンツ)にアクセスするアプリの場合も同様に考え、JavaScriptを有効にしてもよい。「4.9.1.1
assetsまたはresディレクトリに配置したコンテンツのみを表示する」を参照すること。

##### 自社で管理していないサービス

自社で管理していないコンテンツ・サービスは自社が安全を保証できると考えてはならない。それゆえ、アプリのJavaScriptを無効にしなければならない。「4.9.1.3
自社管理以外のコンテンツを表示する」を参照すること。加えて、SDカードのような端末の外部記憶装置に配置されたコンテンツは他のアプリによる書き換えが可能なので、自社が管理しているとは言えない。そのようなコンテンツにアクセスするアプリについてもJavaScriptを無効にしなければならない。

#### 自社管理サービスとの通信にはHTTPSを使用する （必須）

自社管理サービスにアクセスするアプリは、悪意ある第三者によるサービスのなりすましによる被害を防ぎ、対象サービスへ確実に接続する必要がある。そのためには、サービスとの通信にHTTPSを使用する。

詳細は「4.9.2.4 SSL通信のエラーを適切にハンドリングする
（必須）」、「5.4 HTTPSで通信する」を参照すること。

#### Intent経由など、他から受け取ったURLはJavaScriptが有効なWebViewには表示しない （必須）

他のアプリからIntentを受信し、そのIntentのパラメータで渡されたURLをWebViewに表示する実装が多くのアプリで見られる。ここでWebViewのJavaScriptが有効である場合、悪意あるWebページのURLをWebViewで表示してしまい、悪意あるJavaScriptがWebView上で実行されて何らかの被害が生じる可能性がある。この実装の問題点は、安全を保証できない不特定のURLをJavaScriptが有効なWebViewで表示してしまうことである。

サンプルコード「4.9.1.2
インターネット上の自社管理コンテンツを表示する」では、固定URL文字列定数で自社管理コンテンツを指定することで、WebViewで表示するコンテンツを自社管理コンテンツに限定し安全を確保している。

もしIntent等で受け取ったURLをJavaScriptが有効なWebViewで表示したい場合は、そのURLが自社管理コンテンツであることを保証しなければならない。あらかじめアプリ内に自社管理コンテンツURLのホワイトリストを正規表現等で保持しておき、このホワイトリストと照合して合致したURLだけをWebViewで表示することで安全を確保することができる。この場合も、ホワイトリスト登録するURLはHTTPSでなければならないことにも注意が必要だ。

#### SSL通信のエラーを適切にハンドリングする （必須）

HTTPS通信でSSLエラーが発生した場合は、エラーが発生した旨をダイアログ表示するなどの方法でユーザーに通知して、通信を終了しなければならない。

SSLエラーの発生は、サーバー証明書に不備がある可能性、あるいは中間者攻撃を受けている可能性を示唆する。しかし、WebViewには、サービスとの通信時に発生したSSLエラーに関する情報をユーザーに通知する仕組みが備わっていない。そこで、SSLエラーが発生した場合にはその旨をダイアログなどで表示することで、脅威にさらされている可能性があることをユーザーに通知する必要がある。エラー通知の例は、「4.9.1.2
インターネット上の自社管理コンテンツを表示する」のサンプルコードあるいは「4.9.1.3
自社管理以外のコンテンツを表示する」のサンプルコードを参照すること。

また、エラーの通知に加えて、アプリはサービスとの通信を終了しなければならない。特に、次のような実装を行ってはならない。

-   発生したエラーを無視してサービスとの通信を継続する

-   HTTPなどの非暗号化通信を使ってサービスと改めて通信する

HTTP通信/HTTPS通信の詳細は「5.4 HTTPSで通信する」を参照すること。

SSLエラーが発生した際には対象のサーバーと接続を行わないことがWebViewのデフォルトの挙動である。よって、WebViewのデフォルトの挙動にSSLエラーの通知機能を実装することで適切に通信エラーを取り扱うことができる。

### アドバンスト<!-- bbb3af0f -->

#### Android 4.2未満の端末におけるaddJavascriptInterface()に起因する脆弱性について

Android 4.2（API Level
17）未満の端末にはaddJavascriptInterface()に起因する脆弱性があり、JavaScriptからJavaのリフレクションを行うことにより任意のJavaメソッドが実行できてしまう問題が存在する。

そのため、「4.9.2.1
JavaScriptを有効にするのはコンテンツを自社が管理している場合に限定する
（必須）」で解説した通り、自社で管理していないコンテンツ・サービスにアクセスする可能性がある場合は、JavaScriptを無効にする必要がある。

Android 4.2（API Level
17）以降の端末では、Javaのソースコード上で@JavascriptInterfaceというアノテーションが指定されたメソッドしかJavaScriptから操作できないようにAPIが仕様変更され、脆弱性の対策がされた。ただし、自社で管理していないコンテンツ・サービスにアクセスする可能性がある場合は、コンテンツ・サービス提供者が悪意あるJavaScriptを送信する恐れがあるため、JavaScriptを無効化する対策は引き続き必要である。

#### fileスキームに起因する問題について

WebViewをデフォルト設定で使用している場合、fileスキームを利用してアクセスすると当該アプリがアクセス可能なすべてのファイルにアクセスすることが可能になる。この動作を悪用された場合、例えば、JavaScriptからfileスキーム使ったリクエストすることで、アプリの専用フォルダに保存したファイル等を攻撃者に取得されてしまう可能性がある。

対策としては、「4.9.2.1
JavaScriptを有効にするのはコンテンツを自社が管理している場合に限定する
（必須）」で解説した通り、自社で管理していないコンテンツ・サービスにアクセスする可能性がある場合はJavaScriptを無効にする。この対策により意図しないfileスキームによるリクエストが送信されないようにする。

また、Android 4.1（API Level
16）以降の場合、setAllowFileAccessFromFileURLs()およびsetAllowUniversalAccessFromFileURLs()を利用することでfileスキームによるアクセスを禁止することができる。

fileスキームの無効化
```java
		webView = (WebView) findViewById(R.id.webview);
		webView.setWebViewClient(new WebViewUnlimitedClient());
		WebSettings settings = webView.getSettings();
		settings.setAllowUniversalAccessFromFileURLs(false);
		settings.setAllowFileAccessFromFileURLs(false);
```

#### Web Messaging利用時の送信先オリジン指定について
```eval_rst
Android 6.0(API Level 23)において、HTML5 Web Messagingを実現するためのAPIが追加された。Web Messagingは異なるブラウジング・コンテキスト間でデータを送受信するための仕組みであり、HTML5で定義されている [24]_。

.. [24] http://www.w3.org/TR/webmessaging/

WebViewクラスに追加されたpostWebMessage()はWeb Messagingで定義されているCross-domain messagingによるデータ送信を処理するメソッドである。このメソッドは第一引数で指定されたメッセージオブジェクトをWebViewに読み込んでいるブラウジング・コンテキストに対して送信するのだが、その際第二引数として送信先のオリジンを指定する必要がある。指定されたオリジン [25]_ が送信先コンテキストのオリジンと一致しない限りメッセージは送信されない。送信先オリジンを制限することで、意図しない送信先にメッセージを渡してしまうことを防いでいるのである。

.. [25] オリジンとは、URLのスキーム、ホスト名、ポート番号の組み合わせのこと。詳細な定義は http://tools.ietf.org/html/rfc6454 を参照。

ただし、postWebMessage()メソッドではオリジンとしてワイルドカードを指定できることに注意が必要である [26]_。ワイルドカードを指定するとメッセージの送信先オリジンがチェックされず、どのようなオリジンに対してもメッセージを送信してしまう。もしWebViewに悪意のあるコンテンツが読み込まれている状況でオリジンの制限なしに重要なメッセージを送信してしまうと何らかの被害につながる可能性も生じる。WebViewを用いてWeb messagingを行う際は、postWebMessage()メソッドに特定のオリジンを明示的に指定するべきである。

.. [26] Uri.EMPTYおよびUri.parse(\"\")がワイルドカードとして機能する(2016年9月1日版執筆時)
```

Notificationを使用する
----------------------

Androidにはエンドユーザーへのメッセージを通知するNotification機能がある。Notificationを使うと、画面上部のステータスバーと呼ばれる領域に、アイコンやメッセージを表示することができる。

![](media/image53.png)
```eval_rst
.. {width="3.716666666666667in"
.. height="6.603472222222222in"}
```

図 4.10‑1　Notifcationの表示例

Notificationの通知機能は、Android 5.0(API Level
21)で強化され、アプリやユーザー設定によって、画面がロックされている状態であってもNotificationによる通知を表示することが可能になった。ただし、Notificationの使い方を誤ると、端末ユーザー本人にのみ見せるべきプライベートな情報が第三者の目に触れる恐れがある。したがって、プライバシーやセキュリティを考慮して適切に実装を行うことが重要である。

なお、Visibilityが取り得る値とNotificationの振る舞いは以下の通りである。
```eval_rst
==============  ================================================================================
Visibilityの値  Notificationの振る舞い
==============  ================================================================================
Public          | すべてのロック画面上でNotificationが表示される
Private         | すべてのロック画面上でNotifcationが表示されるが、パスワード等で保護された
                | ロック画面（セキュアロック）上では、Notificationのタイトルやテキスト等が
                | 隠される（プライベート情報が隠された公開可能な文に置き換わる）
Secret          | パスワード等で保護されたロック画面（セキュアロック）上では、Notification
                | が表示されなくなる（セキュアロック以外のロック画面ではNotificationは表示
                | される）
==============  ================================================================================
```
### サンプルコード<!-- 2de6db20 -->

Notificationに端末ユーザーのプライベートな情報を含む場合、プライベート情報を取り除いた通知を画面ロック時の表示用に作成し、加えておくこと。

![](media/image54.png)
```eval_rst
.. {width="3.716666666666667in"
.. height="6.603472222222222in"}
```

図 4.10‑2　ロック画面上のNotification

プライベート情報を含んだ通知を行うサンプルコードを以下に示す。

ポイント：

1.  プライベート情報を含んだ通知を行う場合は、公開用（画面ロック時の表示用）のNotification
    を用意する
2.  公開用（画面ロック時の表示用）の Notificationにはプライベート情報を含めない
3.  Visibility を明示的にPrivate に設定して、Notification を作成する
4.  Visibility が Private の場合、プライベート情報を含めて通知してもよい

VisibilityPrivateNotificationActivity.java
```eval_rst
.. literalinclude:: CodeSamples/Notification VisibilityPrivate.VisibilityPrivateNotificationActivity.java
   :language: java
   :encoding: shift-jis
```


### ルールブック<!-- a8692504 -->

Notificationを利用する際には以下のルールを守ること。

1.  Visibilityの設定に依らず、Notificationにはセンシティブな情報を含めない（プライベート情報は例外）（必須）
2.  Visibility PublicのNotificationには プライベート情報を含めない （必須）
3.  （特にVisibility Privateにする場合）Visibility は明示的に設定する （必須）
4.  VisibilityがPrivateのNotificationを利用する場合、VisibilityをPublicにした公開用のNotificationを併せて設定する （推奨）

#### Visibilityの設定に依らず、Notificationにはセンシティブな情報を含めない（プライベート情報は例外） （必須）

Android4.3(API Level
18)以降の端末では、設定画面からユーザーがNotificationの読み取り許可をアプリに与えることができる。許可されたアプリは、全てのNotificationの情報を読み取ることが可能になるため、センシティブな情報をNotificationに含めてはならない。（ただし、プライベート情報はVisibilityの設定によってはNotificationに含めて良い）

Notificationに含まれた情報は、通常はNotificationを送信したアプリを除き、他のアプリから読み取ることはできない。しかし、ユーザーが明示的に許可を与えることで、ユーザーが指定したアプリは全てのNotificationの情報を読み取ることが可能になる。ユーザーが許可を与えたアプリのみがNotificationの情報を読み取れることから、ユーザー自身のプライベート情報をNotificationに含めることは問題ない。一方で、ユーザーのプライベート情報以外のセンシティブな情報（例えば、アプリ開発者のみが知り得る機密情報）をNotificationに含めると、ユーザー自身がNotificationに含まれた情報を読みとろうとしてNotificationへの閲覧をアプリに許可する可能性があるため、利用者のプライベート情報以外のセンシティブな情報を含めることは問題となる。

具体的な方法と条件は、「4.10.3.1
ユーザー許可によるNotificationの閲覧について」を参照の事。

#### Visibility PublicのNotificationには プライベート情報を含めない （必須）

VisibilityがPublicに設定されたNotificationによって通知を行う場合、ユーザーのプライベート情報をNotificationに含めてはならない。VisibilityがPublicに設定されたNotificationは、画面ロック中にもNotificationの情報が表示され、端末に物理的に接近できる第三者がプライベート情報を盗み見るリスクにつながるためである。

VisibilityPrivateNotificationActivity.java
```java
    // 公開用（画面ロック時の表示用）の センシティブな情報を持たない Notification を用意する
    Notification.Builder publicNotificationBuilder = new Notification.Builder(this).setContentTitle("Notification : Public");

    publicNotificationBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
    // 公開用（画面ロック時の表示用）の Notificationにはプライベート情報を含めない
    publicNotificationBuilder.setContentText("Visibility Public : センシティブな情報は含めずに通知");
    publicNotificationBuilder.setSmallIcon(R.drawable.ic_launcher);
```

プライベート情報の典型例としては、ユーザー宛てに送信されたメールやユーザーの位置情報など、「5.5.
プライバシー情報を扱う」で言及されている情報が挙げられる。

#### （特にVisibility Privateにする場合）Visibility は明示的に設定する （必須）

「4.10.2.2 Visibility PublicのNotificationには
プライベート情報を含めない （必須）」の通り、Android 5.0(API Level
21)以降の端末では、画面ロック中にもNotificationが表示されるため、Visibilityの設定が重要であり、デフォルト値に頼らず明示的に設定すること。

現状では、NotificationのVisibilityのデフォルト値はPrivateに設定されており、明示的にPublicを指定しない限りプライベート情報が盗み見られるリスクは発生しない。しかし、Visibilityのデフォルト値が将来変更になる可能性もあり、含める情報の取り扱いを常に意識するためにも、たとえVisiblityをPrivateにする場合であっても、NotificationのVisibilityは明示的に設定することを必須としている。

VisibilityPrivateNotificationActivity.java
```java
        // プライベート情報を含む Notification を作成する
        Notification.Builder priavteNotificationBuilder = new Notification.Builder(this).setContentTitle("Notification : Private");

        // ★ポイント★ 明示的に Visibility を Private に設定して、Notification を作成する
        priavteNotificationBuilder.setVisibility(Notification.VISIBILITY_PRIVATE);
```

####  VisibilityがPrivateのNotificationを利用する場合、VisibilityをPublicにした公開用のNotificationを併せて設定する （推奨）

VisibilityがPrivateに設定されたNotificationを使って通知する場合、画面ロック中に表示される情報を制御するため、VisibilityをPublicにした公開用のNotificationを併せて設定することが望ましい。

VisibilityがPrivateに設定されたNotificationに公開用のNotificationを設定しない場合、画面ロック中にはシステムで用意されたデフォルトの文言が表示されるためセキュリティ上の問題はない。しかし、Notificationに含める情報の取り扱いを常に意識するためにも、公開用のNotificationを明示的に用意し設定することを推奨する。

VisibilityPrivateNotificationActivity.java
```java
    // プライベート情報を含む Notification を作成する
    Notification.Builder privateNotificationBuilder = new Notification.Builder(this).setContentTitle("Notification : Private");

    // ★ポイント★ 明示的に Visibility を Private に設定して、Notification を作成する
    if (Build.VERSION.SDK_INT >= 21)
        privateNotificationBuilder.setVisibility(Notification.VISIBILITY_PRIVATE);
    // ★ポイント★ Visibility が Private の場合、プライベート情報を含めて通知してもよい
    privateNotificationBuilder.setContentText("Visibility Private : Including user info.");
    privateNotificationBuilder.setSmallIcon(R.drawable.ic_launcher);
    // VisibilityがPrivateのNotificationを利用する場合、VisibilityをPublicにした公開用のNotificationを合わせて設定する
    if (Build.VERSION.SDK_INT >= 21)
        privateNotificationBuilder.setPublicVersion(publicNotification);
```

### アドバンスト<!-- 5fe6bbe1 -->

#### ユーザー許可によるNotificationの閲覧について

「4.10.2.1
Visibilityの設定に依らず、Notificationにはセンシティブな情報を含めない（プライベート情報は例外）
（必須）」で述べたように、Android4.3(API Level
18)以降の端末では、ユーザーが許可を与えた場合、指定されたアプリは全てのNotificationの情報を読み取ることが可能になる。ただし、ユーザー許可の対象となるためには、アプリがNotificationListenerServiceを継承したServiceを実装しておく必要がある。

![](media/image55.png)
```eval_rst
.. {width="3.71875in" height="6.604166666666667in"}
```

図 4.10‑3　Notificationの読み取りを設定する「通知へのアクセス」画面

NotificationListenerServiceを使ったサンプルコードを以下に示す。

AndroidManifest.xml
```eval_rst
.. literalinclude:: CodeSamples/NotificationListenerService.app.src.main.AndroidManifest.xml
   :language: xml
   :encoding: shift-jis
```


MyNotificationListenerService.java
```eval_rst
.. literalinclude:: CodeSamples/NotificationListenerService.MyNotificationListenerService.java
   :language: java
   :encoding: shift-jis
```


上記の通り、NotificationListenerServiceを使い、ユーザーの許可を得ることで、Notificationを読み取ることが可能になるが、Notificationに含まれる情報には端末のプライベート情報が含まれることが多いため、取り扱いには十分な注意が必要である。