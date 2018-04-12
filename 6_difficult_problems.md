Difficult Problems
==================

In Android, there are some problems that it is difficult to assure a security by application implementation due to a specification of Android OS or a function which Android OS provides. By being abused by the malicious third party or used by users carelessly, these functions are always holding risks that may lead to security problems like information leakage. In this chapter, by indicating risk mitigation plans that developers can take against these functions, some topics that needs calling attentions, are picked up as articles.

Risk of Information Leakage from Clipboard
------------------------------------------

Copy & paste are the functions which users often use in a casual manner. For example, not a few users use these functions to store curious information or important information to remember in a mail or a web page into a notepad, or to copy and to paste a password from a notepad in which passwords are stored in order not to forget in advance. These are very casual actions at a glance, but actually there\'s a hidden risk that user handling information may be stolen.

The risk is related to mechanism of copy & paste in Android system. The information which was copied by user or application, is once stored in the buffer called Clipboard. The information stored in Clipboard is distributed to other applications when it is pasted by a user or an application. So there is a risk which leads to information leakage in this Clipboard function. It is because the entity of Clipboard is single in a system and any application can obtain the information stored in Clipboard at any time by using ClipboardManager. It means that all the information which user copied/cut, is leaked out to the malicious application.

Hence, application developers need to take measures to minimize the possibility of information leakage, considering the Android OS specifications.

### Sample Code<!-- 1de829ee -->

Roughly speaking, there are two outlooks of counter-measures to mitigate the risk of information leakage form Clipboard.

1. Counter-measure when copying from other applications to your application.

2. Counter-measure when copying from your application to other applications.

Firstly, let us discuss the countermeasure 1 above. Supposing that a user copies character strings from other applications like note pad, Web browser or mailer application, and then paste it to EditText in your application. As it turns out, there\'s no basic counter-measure to prevent from sensitive information leakage due to copy & paste, in this scenario. Since there\'s no function in Android to control copy operations by the third party application.

So, regarding the countermeasure 1, there\'s no method other than explaining users the risk of copying & pasting sensitive information, and just continuing to enlighten users to decrease the actions themselves continuously.

Next discussion is the countermeasure 2 above, supposing that the scenario that a user copies sensitive information displayed in your application. In this case, the sound counter-measure for leakage is to prohibit copying/cutting operations from View (TextView, EditText etc.). If there are no copy/cut functions in View where the sensitive information (like personal information) is input/output, information leakage will never happen from your application via Clipboard.

There are several methods to prohibit copying/cutting. This section herein describes the easy and effective methods: One method is to disable long press View and another method is to delete copy/cut items from menu when selecting character string.

```eval_rst
Necessary of counter-measure can be determined as per the flow of :numref:`Decision flow of counter-measure is required or not.` . In :numref:`Decision flow of counter-measure is required or not.` , \"Input type is fixed to Password attribute\" means, the input type is necessarily either of the followings three when application is running. In this case, no counter-measures are required since copy/cut are prohibited as default.
```

-   InputType.TYPE\_CLASS\_TEXT \|
    InputType.TYPE\_TEXT\_VARIATION\_PASSWORD

-   InputType.TYPE\_CLASS\_TEXT \|
    InputType.TYPE\_TEXT\_VARIATION\_WEB\_PASSWORD

-   InputType.TYPE\_CLASS\_NUMBER \|
    InputType.TYPE\_NUMBER\_VARIATION\_PASSWORD

```eval_rst
.. figure:: media/image86.png
   :name: Decision flow of counter-measure is required or not.

   Decision flow of counter-measure is required or not.
```

The following subsections detail each countermeasure with sample codes.

#### Delete copy/cut from the menu when character string selection

TextView.setCustomSelectionActionModeCallback()メソッドによって、文字列選択時のメニューをカスタマイズできる。これを用いて、文字列選択時のメニューからコピー・カットのアイテムを削除すれば、ユーザーが文字列をコピー・カットすることはできなくなる。

Sample code to delete copy/cut item from menu of character string selection in EditText, is shown as per below.

Points:

1. Delete android.R.id.copy from the menu of character string selection.

2. Delete android.R.id.cut from the menu of character string selection.

UncopyableActivity.java
```eval_rst
.. literalinclude:: CodeSamples/LeakageViaClipboard.UncopyableActivity.java
   :language: java
   :encoding: shift-jis
```

#### Disable Long Click View

Prohibiting copying/cutting can also be realized by disabling Long Click View. Disabling Long Click View can be specified in layout xml file.

Point:

1. Set false to android:longClickable in View to prohibit copy/cut.

unlongclickable.xml
```eval_rst
.. literalinclude:: CodeSamples/UnlongClickableEditview.app.src.main.res.layout.unlongclickable.xml
   :language: xml
   :encoding: shift-jis
```

### Rule Book<!-- d89ca9b9 -->

Follow the rule below when copying sensitive information from your application to other applications.

1. Disabling Copy/Cut Character Strings that Are Displayed in View (Required)

#### Disabling Copy/Cut Character Strings that Are Displayed in View (Required)

If there\'s a View which displays sensitive information in an application and besides the information is allowed to be copied/cut like EditText in the View, the information may be leaked via Clipboard. Therefore, copy/cut must be disabled in View where sensitive information is displayed.

There are two methods to disable copy/cut. One method is to delete items of copy/cut from menu of character string selection, and another method is to disable Long Click View.

Please refer to \"6.1.3.1 Precautions When Applying Rules.\"

### Advanced Topics<!-- ad4d40cb -->

#### Precautions When Applying Rules

In TextView, selecting character string is impossible as default, so normally no counter-measure is required, but in some cases copying is possible depends on application\'s specifications. The possibility of selecting/copying character strings can be dynamically determined by using TextView.setTextIsSelectable() method. When setting copying possible in TextView, investigate the possibility that any sensitive information is displayed in TextView, and if there are any possibilities, it should not be set as possible to copy.

In addition, described in the decision flow of \"6.1.1Sample Code\" regarding EditText which is input type (InputType.TYPE\_CLASS\_TEXT \| InputType.TYPE\_TEXT\_VARIATION\_PASSWORD etc.), supposing password input, normally any counter-measures are not required since copying character strings are prohibited as default. However, as described in \"5.1.2.2 Provide the Option to Display Password in a Plain Text (Required),\" when the option to \[display password in a plain text\] is prepared, in case of displaying password in a plain text, input type will change and copy/cut is enabled. So the same counter-measure should be required.

Note that, developers should also take usability of application into consideration when applying rules. For example, in the case of View which user can input text freely, if copy/cut is disabled because there is the slight possibility that sensitive information is input, users may feel inconvenience. Of course, the rule should unconditionally be applied to View which treats highly important information or independent sensitive information, but in the case of View other than those, the following questions will help developers to understand how properly to treat View.

- Prepare some other component for the exclusive use of sensitive information

- Send information with alternative methods when the pasted-to application is obvious

- Call users for cautions about inputting/outputting information

- Reconsider the necessity of View

The root cause of the information leakage risk is that the specifications of Clipboard and ClipboardManager in Android OS leave the security risk out of consideration. Application developers need to create higher quality applications in terms of user integrity, usability, functions, and so forth.

#### Operating Information Stored in Clipboard

As mentioned in \"6.1 Risk of Information Leakage from Clipboard,\" an application can manipulate information stored in Clipboard by using ClipboardManager. In addition, there is no need to set particular Permission for using ClipboardManager and thus the application can use ClipboardManager without being recognized by user.

Information, called ClipData, stored in Clipboard can be obtained with ClipboardManager.getPrimaryClip() method. If a listener is registered to ClipboardManager by ClipboardManager.addPrimaryClipChangedListener() method implementing OnPrimaryClipChangedListener, the listener is called every time copy/cut operations occurred by user. Therefore ClipData can be got without overlooking the timing. Listener call is executed when copy/cut operations occur in any application regardless.

The following shows the source code of Service, which gets ClipData whenever copy/cut is executed in a device and displays it through Toast. You can realize that information stored in Clipboard is leaked out doe to simple codes as follows. It\'s necessary to pay attention that the sensitive information is not taken at least by the following source code.

ClipboardListeningService.java
```eval_rst
.. literalinclude:: CodeSamples/ClipboardListening.ClipboardListeningService.java
   :language: java
   :encoding: shift-jis
```

Next, below shows an example code of Activity which uses ClipboardListeningService touched in the above.

ClipboardListeningActivity.java
```eval_rst
.. literalinclude:: CodeSamples/ClipboardListening.ClipboardListeningActivity.java
   :language: java
   :encoding: shift-jis
```

Thus far we have introduced methods for obtaining data stored on the Clipboard. It is also possible to use the ClipboardManager.setPrimaryClip() method to store new data on the Clipboard.

Note that setPrimaryClip() method will overwrite the information stored in Clipboard, therefore the information stored by user\'s copy/cut may be lost. When providing custom copy/cut functions with these methods, it\'s necessary to design/implement in order not that the contents stored in Clipboard are changed to unexpected contents, by displaying a dialogue to notify the contents are to be changed, according the necessity.
