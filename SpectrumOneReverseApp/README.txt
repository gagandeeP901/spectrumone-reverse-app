SPECTRUM ONE REVERSE - Android App (website: https://spectrumonereverse.com)

STEPS
1. Android Studio (latest) me File > Open > is folder ko select karo. Gradle sync hone do.
2. LOGO: website ka favicon/logo download karo
   https://spectrumonereverse.com/wp-content/uploads/2026/10/Favicon-300x300.png
   Android Studio me: app par right-click > New > Image Asset > Launcher Icons (Adaptive and Legacy)
   > Source Asset me ye PNG select karo > Finish. (Ye placeholder icon replace kar dega.)
3. Colors: app/src/main/res/values/colors.xml me brand_primary ko website ke color se match karo.
4. Test: emulator ya phone par Run karo.
5. Release: Build > Generate Signed App Bundle > Android App Bundle (.aab). Keystore bana kar SAFE rakho.
6. Play Console me upload karo. Privacy Policy URL: https://spectrumonereverse.com/privacy/

NOTES
- Package name: com.spectrumonereverse.app (Play Console me unique hona chahiye; chaho to badal sakte ho)
- targetSdk Play ki current requirement ke hisaab se app/build.gradle.kts me update karna.
- Bottom bar: Home, Calculator, Rates, Call (888-813-5655).
