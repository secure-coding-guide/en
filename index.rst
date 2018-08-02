.. Secure Coding Guide documentation master file, created by
   sphinx-quickstart on Thu Oct 19 15:21:07 2017.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

Android Application Secure Design/Secure Coding Guidebook
=========================================================

.. image:: media/image1.png

| **February 1, 2018 Edition**
| **Japan Smartphone Security Association (JSSEC)**
| **Secure Coding Working Group**
| 

- The content of this guide is up to date as of the time of publication, but standards and environments are constantly evolving. When using sample code, make sure you are adhering to the latest coding standards and best practices.

- JSSEC and the writers of this guide are not responsible for how you use this document. Full responsibility lies with you, the user of the information provided.

- Android™ is a trademark or a registered trademark of Google Inc. The company names, product names and service names appearing in this document are generally the registered trademarks or trademarks of their respective companies. Further, the registered trademark ®, trademark (TM) and copyright © symbols are not used throughout this document.

- Parts of this document are copied from or based on content created and provided by Google, Inc. They are used here in accordance with the provisions of the Creative Commons Attribution 3.0 License 

Android Application Secure Design/Secure Coding Guidebook
---------------------------------------------------------

.. image:: media/image3.png
   :width: 0.8131944444444444in
   :height: 0.9840277777777777in
   :align: left
.. image:: media/image4.png
   :width: 0.8131944444444444in
   :height: 0.9840277777777777in
   :align: right

| - Beta version -
| February 1, 2018
| 
| Japan Smartphone Security Association
| Secure Coding Working Group
| 


.. toctree::
   :numbered: 4
   :maxdepth: 2
   :caption: Index:

   1_introduction
   2_composition_of_the_guidebook
   3_basic_knowledge_of_secure_design_and_secure_coding
   4_using_technology_in_a_safe_way
   5_how_to_use_security_functions
   6_difficult_problems



Indices and tables
==================

* :ref:`genindex`
* :ref:`modindex`
* :ref:`search`


Revision history
================

2014-04-01
    Initial English Edition
2014-07-01
    Added new articles below
        - 5.5 Handling privacy data
        - 5.6 Using Cryptography
2015-06-01
    We have reviewed the entire document in accordance with the following policy
        - Change of development environment (Eclipse -> Android Studio)
        - Responding to Android latest version Lollipop
        - Change of API Level (8 or later -> 15 or later)
2016-02-01
    Added new articles below
        - 4.10 Using Notifications
        - 5.7 Using fingerprint authentication features
    Revised article below
        - 5.2 Permission and Protection Level
2016-09-01
    Revised articles below
        - 2.5 Steps to Install Sample Codes into Android Studio
        - 5.4 Communicating via HTTPS
        - 5.6 Using Cryptography
2017-02-01
    Added new articles below
        - 4.6.3.5 Revised specifications in Android 7.0 (API Level 24) for accessing specific directories on external storage media
        - 5.4.3.7 Network Security Configuration
    Revised articles below
        - 4.1 Creating/Using Activities
        - 4.2 Receiving/Sending Broadcasts
        - 4.4 Creating/Using Services
        - 4.5 Using SQLite
        - 4.6 Handling Files
    Deleted the section below
        - 4.8.3.4 BuildConfig.DEBUG Should Be Used in ADT 21 or Later
    We have reviewed the entire document in accordance with the following policy
        - All discussions in the main text concerning Android 4.0.3 (API Level 15) and earlier versions have been deleted or moved to footnotes.
2018-02-01
    Added new articles below
        - 4.1.3.7 The Autofill framework
        - 5.3.3.3 Cases in which Authenticator accounts with non-matching signatures may be read in Android 8.0 (API Level 26) or later
        - 5.4.3.8 (Column): Transitioning to TLS1.2 for secure connections
        - 5.5.3.3 Version-dependent differences in handling of Android IDs
    Revised articles below
        - 4.2 Receiving/Sending Broadcast
        - 5.2 Permission and Protection Level
        - 5.3 Add In-house Accounts to Account Manager
        - 5.4 Communicating via HTTPS
        - 5.5 Handling privacy data

    Note: For a detailed description of these revisions, see Section "1.4 Correction articles of February 1, 2018 edition"

In preparing a new version for public release, we have revised the content of this guidebook based on opinions, comments and suggestions received from readers.

Published by
------------

Japan Smartphone Security Association Secure Coding Working Group, Smartphone Technology Committee

Leader
~~~~~~

Akira Ando

    Sony Digital Network Applications, Inc.

Member
~~~~~~

=================== =====================================================
 Ken Okuyma         Android Security Japan
 Eiji Hoshimoto	    Software Research Associates, Inc.
 Akihiro Shiota     NTT DATA Corporation
 Shigenori Takei    NTT Software Corporation
 Ikuya Fukumoto     Japan Computer Emergency Response Team Coordination Center (JPCERT/CC)
 Mariko Yoshida     Sony Digital Network Applications, Inc.
 Nobuaki Yamaguchi  Sony Digital Network Applications, Inc.
 Jun Ogiso          Sony Digital Network Applications, Inc.
 Junki Hisamoto     Sony Digital Network Applications, Inc.
 Masahiro Kasahara  SoftBank Corp.
 Ito Takefumi       Nihon System Kaihatsu Co., Ltd.
 Shigeru Yatabe     Fomalhaut Techno Solutions
=================== =====================================================

(In no particular order)

Authors of February 1, 2017 Edition
-----------------------------------

Leader
~~~~~~

Ken Okuyama

    Sony Digital Network Applications, Inc.

Member
~~~~~~

===================== =====================================================
  Shigeharu Araki     Android Security Japan
  Eiji Shimano        Android Security Japan
  Akihiro Shiota      NTT DATA Corporation
  Shigenori Takei     NTT Software Corporation
  Ikuya Fukumoto      Software Research Associates, Inc.
  Tomomi Ohuchi       Software Research Associates, Inc.
  Yoichi Yamanoi      Software Research Associates, Inc.
  Hidenori Yamaji     Sony Corporation
  Akira Ando          Sony Digital Network Applications, Inc.
  Jun Ogiso           Sony Digital Network Applications, Inc.
  Masaru Matsunami    Sony Digital Network Applications, Inc.
  Tetsuya Takahashi   SQUARE ENIX CO., LTD.
  Gaku Taniguchi      Tao Software, Inc.
===================== =====================================================

(In no particular order)

Authors of September 1, 2016 Edition
------------------------------------

Leader
~~~~~~

Masaru Matsunami

    Sony Digital Network Applications, Inc.

Member
~~~~~~

===================== =====================================================
  Shigeharu Araki     Android Security Japan
  Shigenori Takei     NTT Software Corporation
  Ikuya Fukumoto      Software Research Associates, Inc.
  Tomomi Ohuchi       Software Research Associates, Inc.
  Hidenori Yamaji     Sony Corporation
  Akira Ando          Sony Digital Network Applications, Inc.
  Jun Ogiso           Sony Digital Network Applications, Inc.
  Ken Okuyama         Sony Digital Network Applications, Inc.
  Mitake Ohtani       Sony Digital Network Applications, Inc.
  Daisuke Mitsuzono   Nihon System Kaihatsu Co., Ltd.
  Eiji Shimano        Tao Software, Inc.
  Gaku Taniguchi      Tao Software, Inc.
===================== =====================================================

(In no particular order)

Authors of February 1, 2016 Edition
-----------------------------------

Leader
~~~~~~

Masaru Matsunami

    Sony Digital Network Applications, Inc.

Member
~~~~~~

===================== =====================================================
  Masaomi Adachi      Android Security Japan
  Tohru Ohzono        Cisco Systems, Inc.
  Shigenori Takei     NTT Software Corporation
  Masahiro Kasahara   SoftBank Mobile Corp.
  Eiji Hoshimoto      Software Research Associates, Inc.
  Ikuya Fukumono      Software Research Associates, Inc.
  Akira Ando          Sony Digital Network Applications, Inc.
  Ken Okuyama         Sony Digital Network Applications, Inc.
  Mitake Ohtani       Sony Digital Network Applications, Inc.
  Muneaki Nishimura   Sony Digital Network Applications, Inc.
  Setsuko Kaji        Sony Digital Network Applications, Inc.
  Taeko Ito           Sony Digital Network Applications, Inc.
  Hidenori Yamaji     Sony Mobile Communications Inc.
  Eiji Shimano        Tao Software, Inc.
  Gaku Taniguchi      Tao Software, Inc.
===================== =====================================================


(In no particular order)

Authors of June 1, 2015 Edition
-------------------------------

Leader
~~~~~~

Masaru Matsunami

    Sony Digital Network Applications, Inc.

Member
~~~~~~

==================== =======================================
  Tohru Ohzono       Cisco Systems, Inc.
  Akio Kondo         BRILLIANTSERVICE co., Ltd.
  Kazuma Mitake      BRILLIANTSERVICE co., Ltd.
  Kyosuke Imanishi   BRILLIANTSERVICE co., Ltd.
  Masato Shintani    BRILLIANTSERVICE co., Ltd.
  Naohiko Shimura    BRILLIANTSERVICE co., Ltd.
  Ryuji Fujita       BRILLIANTSERVICE co., Ltd.
  Shohei Hara        BRILLIANTSERVICE co., Ltd.
  Tomoyuki Fujisawa  BRILLIANTSERVICE co., Ltd.
  Yutaka Kawahara    BRILLIANTSERVICE co., Ltd.
  Shigeru Yatabe     Fomalhaut Techno Solutions
  Naonobu Yatsukawa  Nihon Unisys, Ltd.
  Shigenori Takei    NTT Software Corporation
  Masahiro Kasahara  SoftBank Mobile Corp.
  Eiji Hoshimoto     Software Research Associates, Inc.
  Akira Ando         Sony Digital Network Applications, Inc.
  Ken Okuyama        Sony Digital Network Applications, Inc.
  Muneaki Nishimura  Sony Digital Network Applications, Inc.
  Eiji Shimano       Tao Software, Inc.
  Gaku Taniguchi     Tao Software, Inc.
==================== =======================================

(In no particular order)

Authors of July 1, 2014 English Edition
---------------------------------------

Leader
~~~~~~

Masaru Matsunami

    Sony Digital Network Applications, Inc.

Member
~~~~~~

=================== =======================================
Tohru Ohzono        Cisco Systems, Inc.
Shigeru Yatabe      Fomalhaut Techno Solutions
Keisuke Takemori    KDDI CORPORATION
Takamasa Isohara    KDDI CORPORATION
Naonobu Yatsukawa   Nihon Unisys, Ltd.
Shigenori Takei     NTT Software Corporation
Masahiro Kasahara   SoftBank Mobile Corp.
Eiji Hoshimoto      Software Research Associates, Inc.
Tsutomu Kumazawa    Software Research Associates, Inc.
Akira Ando          Sony Digital Network Applications, Inc.
Ken Okuyama         Sony Digital Network Applications, Inc.
Setsuko Kaji        Sony Digital Network Applications, Inc.
Taeko Ito           Sony Digital Network Applications, Inc.
Yoshinori Kataoka   Sony Digital Network Applications, Inc.
Eiji Shimano        Tao Software, Inc.
Gaku Taniguchi      Tao Software, Inc.
Michiyoshi Sato     Tokyo System House Co., Ltd.
=================== =======================================

(In no particular order)

Authors of April 1, 2014 English Edition
----------------------------------------

Leader
~~~~~~

Masaru Matsunami

    Sony Digital Network Applications, Inc.

Member
~~~~~~

===================== ======================================================================
Tomoyuki Hasegawa     Android Security Japan
Mayumi Nishiyama      BJIT Inc.
Tohru Ohzono          Cisco Systems, Inc.
Masaki Kubo           Japan Computer Emergency Response Team Coordination Center (JPCERT/CC)
Daniel Burrowes       Kobe Digital Labo Inc.
Zachary Mathis        Kobe Digital Labo Inc.
Renta Futamura        NextGen, Inc.
Naonobu Yatsukawa     Nihon Unisys, Ltd.
Shigenori Takei       NTT Software Corporation
Ikuya Fukumono        Software Research Associates, Inc.
Tsutomu Kumazawa      Software Research Associates, Inc.
Akira Ando            Sony Digital Network Applications, Inc.
Hiroko Nakajima       Sony Digital Network Applications, Inc.
Ken Okuyama           Sony Digital Network Applications, Inc.
Satoshi Fujimura      Sony Digital Network Applications, Inc.
Setsuko Kaji          Sony Digital Network Applications, Inc.
Taeko Ito             Sony Digital Network Applications, Inc.
Yoshinori Kataoka     Sony Digital Network Applications, Inc.
Hidenori Yamaji       Sony Mobile Communications Inc.
Takuya Nishibayashi   Sony Mobile Communications Inc.
Koji Isoda            Symantec Japan, Inc.
Gaku Taniguchi        Tao Software, Inc.
Michiyoshi Sato       Tokyo System House Co., Ltd.
===================== ======================================================================

Authors of April 1, 2013 Japanese Edition
-----------------------------------------

Leader
~~~~~~

Masaru Matsunami

    Sony Digital Network Applications, Inc.

Member
~~~~~~

=================== ======================================================================
Masaomi Adachi      Android Security Japan
Tomoyuki Hasegawa   Android Security Japan
Yuki Abe            Software Research Associates, Inc.
Tomomi Oouchi       Software Research Associates, Inc.
Tsutomu Kumazawa    Software Research Associates, Inc.
Toshimi Sawada      Software Research Associates, Inc.
Kiyoshi Hata        Software Research Associates, Inc.
Youichi Higa        Software Research Associates, Inc.
Yuu Fukui           Software Research Associates, Inc.
Ikuya Fukumoto      Software Research Associates, Inc.
Eiji Hoshimoto      Software Research Associates, Inc.
Shun Yokoi          Software Research Associates, Inc.
Takakazu Yoshizawa  Software Research Associates, Inc.
Takeshi Fujiwara    NRI SecureTechnologies, Ltd.
Shigenori Takei     NTT Software Corporation
Masaki Kubo         Japan Computer Emergency Response Team Coordination Center(JPCERT/CC)
Hiroshi Kumagai     Japan Computer Emergency Response Team Coordination Center(JPCERT/CC)
Yozo Toda           Japan Computer Emergency Response Team Coordination Center(JPCERT/CC)
Tohru Ohzono        Cisco Systems, Inc.
Toru Asano          Sony Digital Network Applications, Inc.
Akira Ando          Sony Digital Network Applications, Inc.
Ryohji Ikebe        Sony Digital Network Applications, Inc.
Jun Ogiso           Sony Digital Network Applications, Inc.
Ken Okuyama         Sony Digital Network Applications, Inc.
Yoshinori Kataoka   Sony Digital Network Applications, Inc.
Muneaki Nishimura   Sony Digital Network Applications, Inc.
Koji Furusawa       Sony Digital Network Applications, Inc.
Kenji Yamaoka       Sony Digital Network Applications, Inc.
Gaku Taniguchi      Tao Software, Inc.
Naonobu Yatsukawa   Nihon Unisys, Ltd.
Shigeru Yatabe      Fomalhaut Techno Solutions
=================== ======================================================================

(In no particular order)

Authors of November 1, 2012 Japanese Edition
--------------------------------------------

Leader
~~~~~~

Masaru Matsunami

    Sony Digital Network Applications, Inc.

Member
~~~~~~

=================== ======================================================================
Katsuhiko Sato      Android Security Japan
Nakaguchi Akihiko   Android Security Japan
Tomomi Oouchi       Software Research Associates, Inc.
Naoyuki Ohira       Software Research Associates, Inc.
Tsutomu Kumazawa    Software Research Associates, Inc.
Miki Sekikawa       Software Research Associates, Inc.
Seigo Nakano        Software Research Associates, Inc.
Youichi Higa        Software Research Associates, Inc.
Ikuya Fukumoto      Software Research Associates, Inc.
Eiji Hoshimoto      Software Research Associates, Inc.
Shoichi Yasuda      Software Research Associates, Inc.
Tadayuki Yahiro     Software Research Associates, Inc.
Takakazu Yoshizawa  Software Research Associates, Inc.
Shigenori Takei     NTT Software Corporation
Keisuke Takemori    KDDI CORPORATION
Masaki Kubo         Japan Computer Emergency Response Team Coordination Center(JPCERT/CC)
Hiroshi Kumagai     Japan Computer Emergency Response Team Coordination Center(JPCERT/CC)
Yozo Toda           Japan Computer Emergency Response Team Coordination Center(JPCERT/CC)
Tohru Ohzono        Cisco Systems, Inc.
Toru Asano          Sony Digital Network Applications, Inc.
Akira Ando          Sony Digital Network Applications, Inc.
Ryohji Ikebe        Sony Digital Network Applications, Inc.
Shigeru Ichikawa    Sony Digital Network Applications, Inc.
Mitake Ohtani       Sony Digital Network Applications, Inc.
Jun Ogiso           Sony Digital Network Applications, Inc.
Ken Okuyama         Sony Digital Network Applications, Inc.
Yoshinori Kataoka   Sony Digital Network Applications, Inc.
Ikue Sato           Sony Digital Network Applications, Inc.
Muneaki Nishimura   Sony Digital Network Applications, Inc.
Kazuo Yamaoka       Sony Digital Network Applications, Inc.
Takeru Kikkawa      Sony Digital Network Applications, Inc.
Gaku Taniguchi      Tao Software, Inc.
Eiji Shimano        Tao Software, Inc.
Hisao Kitamura      Tao Software, Inc.
Takao Yamakawa      Japan Online Game Association
Masaki Ishihara     Nihon System Kaihatsu Co., Ltd.
Yasuaki Mori        Nihon System Kaihatsu Co., Ltd.
Naonobu Yatsukawa   Nihon Unisys, Ltd.
Shigeru Yatabe      Fomalhaut Techno Solutions
Shigeki Fujii       UNIADEX, Ltd.
=================== ======================================================================

(In no particular order)

Authors of June 1, 2012 Japanese Edition
----------------------------------------

Leader
~~~~~~

Masaru Matsunami

    Sony Digital Network Applications, Inc.

Member
~~~~~~

=================== ======================================================================
Katsuhiko Sato      Android Security Japan
Tomomi Oouchi       Software Research Associates, Inc.
Youichi Higa        Software Research Associates, Inc.
Eiji Hoshimoto      Software Research Associates, Inc.
Shigenori Takei     NTT Software Corporation
Masaaki Chida       GREE, Inc.
Masaki Kubo         Japan Computer Emergency Response Team Coordination Center(JPCERT/CC)
Hiroshi Kumagai     Japan Computer Emergency Response Team Coordination Center(JPCERT/CC)
Yozo Toda           Japan Computer Emergency Response Team Coordination Center(JPCERT/CC)
Tohru Ohzono        Cisco Systems, Inc.
Yoichi Taguchi      System House. ING Co., Ltd.
Masahiko Sakamoto   Secure Sky Technology, Inc.
Akira Ando          Sony Digital Network Applications, Inc.
Shigeru Ichikawa    Sony Digital Network Applications, Inc.
Ken Okuyama         Sony Digital Network Applications, Inc.
Shigeru Ichikawa    Sony Digital Network Applications, Inc.
Ken Okuyama         Sony Digital Network Applications, Inc.
Ikue Sato           Sony Digital Network Applications, Inc.
Muneaki Nishimura   Sony Digital Network Applications, Inc.
Kazuo Yamaoka       Sony Digital Network Applications, Inc.
Gaku Taniguchi      Tao Software, Inc.
Eiji Shimano        Tao Software, Inc.
Hisao Kitamura      Tao Software, Inc.
Michiyoshi Sato     Tokyo System House Co., Ltd.
Masakazu Hattori    Trend Micro Incorporated.
Naonobu Yatsukawa   Nihon Unisys, Ltd.
Shigeru Yatabe      Fomalhaut Techno Solutions
Shigeki Fujii       UNIADEX, Ltd.
=================== ======================================================================

(In no particular order)
