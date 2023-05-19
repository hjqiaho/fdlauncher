############################# 自己的Model不被混淆 ###################
-keep class **.*model*.** {*;}
-keep class **.*response*.** {*;}
-keep class **.*bean*.** {*;}
-keep class **.*entity*.** {*;}