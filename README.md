# Sales Dashboard - Android APK

Saari files ek hi jagah (root) me hain, kisi folder ki zaroorat nahi.
Workflow (`build.yml`) build se pehle khud sahi folders bana leti hai.

## Tareeqa
1. GitHub par naya repository banayen: https://github.com/new
2. Is folder ki saari files **Add file > Upload files** se upload karen
   (ek saath select kar sakte hain, folder nahi chahiye) aur Commit karen.
3. **Actions** tab > **set up a workflow yourself** kholen, naam `build.yml` rakhen,
   `build.yml` ka text paste karen aur Commit karen.
4. Actions me **Build APK** chalne den (ya Run workflow dabayen).
5. Khatam hone par run ke neeche **Artifacts** se **SalesDashboard-APK** download karen.
   Us zip me `app-debug.apk` hai. Phone me install karen.

`index.html` wohi page hai jo app dikhati hai. Naya design aaye to sirf us file ko badlen.
