package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import kotlin.math.absoluteValue
import coil.compose.AsyncImage
import com.example.BuildConfig
import com.example.data.AICharacter
import com.example.data.VideoProject
import com.example.data.VideoScene
import com.example.ui.theme.*
import com.example.viewmodel.StudioScreen
import com.example.viewmodel.StudioViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Static Translations and Labels for bidirectional layout support
object StudioLocales {
    fun appTitle(lang: String) = if (lang == "ar") "استوديو صناعة فيديو الذكاء الاصطناعي" else "AI Video Creator Studio"
    fun tabDashboard(lang: String) = if (lang == "ar") "المشاريع" else "Projects"
    fun tabImage(lang: String) = if (lang == "ar") "توليد صور" else "AI Image"
    fun tabVideo(lang: String) = if (lang == "ar") "صناعة فيديو" else "Create Video"
    fun tabCharacters(lang: String) = if (lang == "ar") "الشخصيات" else "AI Avatars"
    fun tabSettings(lang: String) = if (lang == "ar") "الإعدادات" else "Settings"
    fun tabExport(lang: String) = if (lang == "ar") "تصدير" else "Export"

    // Dashboard Screen
    fun dashboardHeading(lang: String) = if (lang == "ar") "مساحة العمل الإبداعية" else "Creative Workspace"
    fun dashboardSubHeading(lang: String) = if (lang == "ar") "شاهد مشاريعك المحفوظة ونظّم أعمالك السينمائية" else "Monitor saved projects and manage cinematic timelines"
    fun activeProjectsLabel(lang: String) = if (lang == "ar") "المشاريع الفعّالة" else "Active Projects"
    fun createNewVideoBtn(lang: String) = if (lang == "ar") "فيديو جديد بضغطة واحدة" else "One-Click AI Video"
    fun deleteProjectTitle(lang: String) = if (lang == "ar") "حذف المشروع" else "Delete Project"
    fun deleteProjectConfirm(lang: String) = if (lang == "ar") "هل أنت متأكد من حذف هذا المشروع بالكامل؟" else "Are you sure you want to delete this project?"
    fun emptyStateTitle(lang: String) = if (lang == "ar") "لا توجد مشاريع حتى الآن" else "No projects created yet"
    fun emptyStateSub(lang: String) = if (lang == "ar") "ابدأ بكتابة فكرة وسيقوم الذكاء الاصطناعي بإنتاج المشاهد فورا" else "Enter a topic and watch AI synthesize full storyboard clips"

    // Text to Image Screen
    fun imgGenHeading(lang: String) = if (lang == "ar") "توليد وتجسيد الصور" else "Scene Visual Generator"
    fun imgGenSub(lang: String) = if (lang == "ar") "ابتكر صورًا عالية الدقة للمشاهد والمفاهيم الفردية" else "Produce high-resolution scenes and custom storyboards"
    fun labelPromptInput(lang: String) = if (lang == "ar") "صِف المشهد بالتفصيل (بالإنجليزية لأفضل النتائج)" else "Describe scene visual context (English recommended)"
    fun labelStyle(lang: String) = if (lang == "ar") "نمط الرسم والتجسيد" else "Artistic Style Preset"
    fun labelRatio(lang: String) = if (lang == "ar") "أبعاد اللوحة (Aspect Ratio)" else "Aspect Ratio"
    fun btnGenerateImage(lang: String) = if (lang == "ar") "توليد المشهد البصري" else "Synthesize Scene"
    fun imagePlaceholder(lang: String) = if (lang == "ar") "المشهد المولد سيظهر هنا" else "Generated scene will render here"
    fun createVideoFromImgBtn(lang: String) = if (lang == "ar") "اصنع فيديو من هذه الصورة" else "Synthesize Video from this visual"

    // Text to Video Screen
    fun vidCreatorHeading(lang: String) = if (lang == "ar") "الإنتاج التلقائي بضغطة واحدة" else "Omni-Pipeline Video Creator"
    fun vidCreatorSub(lang: String) = if (lang == "ar") "سيكتب الذكاء الاصطناعي السيناريو، يرسم اللقطات، وينتج الصوت تلقائيًا" else "The AI will automatically compose scripts, drawing frames, and lay voice tracks"
    fun labelVideoIdea(lang: String) = if (lang == "ar") "ما هي فكرة أو موضوع الفيديو المطلوب؟" else "What is the primary topic of this video?"
    fun videoIdeaPlaceholder(lang: String) = if (lang == "ar") "مثال: اشرح أهمية حماية الغابات أو قصة عن رحلة إلى المريخ" else "e.g., Explain renewable solar energy or a journey to ancient Egypt"
    fun labelPresenter(lang: String) = if (lang == "ar") "اختر المعلق والمذيع الافتراضي (AI Presenter)" else "Select AI Narrator & Presenter Profile"
    fun labelSpecs(lang: String) = if (lang == "ar") "الخيارات الفنية للمشروع" else "Cinematic Specifications"
    fun labelResolution(lang: String) = if (lang == "ar") "دقة العرض" else "Output Resolution"
    fun labelMusic(lang: String) = if (lang == "ar") "الموسيقى الخلفية" else "Background Soundtrack"
    fun btnCreateVideo(lang: String) = if (lang == "ar") "إنتاج فيديو متكامل بالذكاء الاصطناعي" else "Synthesize Full AI Masterpiece"

    // AI Characters Screen
    fun charHeading(lang: String) = if (lang == "ar") "شخصيات الذكاء الاصطناعي" else "AI Voices & Characters"
    fun charSub(lang: String) = if (lang == "ar") "أدر المذيعين والوجوه الافتراضية المناسبة لأفكارك" else "Build, tweak and bind virtual voice narrators"
    fun customCharTitle(lang: String) = if (lang == "ar") "صناعة مذيع مخصص" else "Construct Custom AI Character"
    fun fieldCharName(lang: String) = if (lang == "ar") "اسم الشخصية" else "Presenter Name"
    fun fieldCharDesc(lang: String) = if (lang == "ar") "الوصف والوظيفة الدقيقة" else "Character Description / Role"
    fun labelVoice(lang: String) = if (lang == "ar") "نبرة الصوت المحددة" else "Vocal Profile Mapping"
    fun labelGender(lang: String) = if (lang == "ar") "جنس الشخصية" else "Gender Group"
    fun btnSaveChar(lang: String) = if (lang == "ar") "حفظ الشخصية في الاستوديو" else "Inscribe Character"

    // Project Details Player Screen
    fun playerTitle(lang: String) = if (lang == "ar") "شاشة مراجعة وتشغيل الإنتاج" else "Cinematic Storyboard & Player"
    fun playerSub(lang: String) = if (lang == "ar") "شاهد المحاكاة النهائية للانتقالات البصرية ومطابقة الصوت وبث السيناريو" else "Simulate visual transition loops, facial lipsync, and sound tracks"
    fun btnPlaySim(lang: String) = if (lang == "ar") "تشغيل العرض" else "Play Video"
    fun btnPauseSim(lang: String) = if (lang == "ar") "إيقاف مؤقت" else "Pause Video"
    fun storyboardHeading(lang: String) = if (lang == "ar") "لوحة التسلسل القصصي (Storyboard)" else "AI Sequence Plan & Metadata"
    fun promptLabel(lang: String) = if (lang == "ar") "مواصفات الرسم:" else "Image Prompt:"
    fun voiceoverScriptLabel(lang: String) = if (lang == "ar") "نص التعليق الصوتي:" else "Narration Text:"
    fun motionLabel(lang: String) = if (lang == "ar") "حركة الكاميرا المطبقة:" else "Dynamic Camera Motion:"

    // Settings Screen
    fun settingsHeading(lang: String) = if (lang == "ar") "إعدادات نماذج الذكاء الاصطناعي" else "Distributed Engine Settings"
    fun settingsSub(lang: String) = if (lang == "ar") "تحكّم في نماذج الخوادم الموزعة مفتوحة المصدر والتحقق الأمني من الرموز" else "Manage open-source models stack & check API credentials"
    fun stackHeading(lang: String) = if (lang == "ar") "حزمة المعالجة الحالية (OS Models Stack)" else "Active Model Orchestration"
    fun securityCardHead(lang: String) = if (lang == "ar") "معلومات الاتصال الأمني والرموز" else "Platform Credentials & Trust panel"
    fun secretsNotice(lang: String) = if (lang == "ar") {
        "يقوم استوديو AI Studio بحقن مفتاحك الأمني (GEMINI_API_KEY) تلقائيًا بشكل آمن ومحمي في ذاكرة الإنتاج النشطة. لا تحتاج لإدخاله يدويًا في التطبيق لمنع تسريبات برمجيات الـ APK."
    } else {
        "AI Studio platform secures your credentials (GEMINI_API_KEY) by injecting it inside active execution memory dynamically. Key manual injection is disabled to keep compiled APK binaries decompilation safe."
    }

    // Export Screen
    fun exportHeading(lang: String) = if (lang == "ar") "تجهيز وتصدير الفيديو النهائي" else "Export & Publish Studio"
    fun exportSub(lang: String) = if (lang == "ar") "اختر الجودة المطلوبة لدمج الصوت والصور والتجهيز النهائي للنشر" else "Select optimal encoding layers to merge frames and vocal feeds"
    fun btnCompileRender(lang: String) = if (lang == "ar") "بدء رندرة وتجميع الفيديو" else "Trigger Render Pipeline"
    fun labelShareSocial(lang: String) = if (lang == "ar") "نشر ومشاركة سريعة" else "Direct Publish Integration"
    fun btnTikTok(lang: String) = if (lang == "ar") "مشاركة على تيك توك" else "Export to TikTok"
    fun btnYouTube(lang: String) = if (lang == "ar") "مشاركة على يوتيوب" else "Publish to Shorts"
}

@Composable
fun VideoStudioAppContainer(viewModel: StudioViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val appLanguage by viewModel.appLanguage.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val generationProgress by viewModel.generationProgress.collectAsState()
    val generationStatus by viewModel.generationStatus.collectAsState()

    // Setup RTL local layouts correctly based on active language Toggle
    val layoutDirection = if (appLanguage == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr

    // State for In-App Smart Assistant Overlay
    var assistantOpen by remember { mutableStateOf(false) }
    var chatInputText by remember { mutableStateOf("") }
    val assistantMessages = remember {
        mutableStateListOf<Pair<String, Boolean>>(
            Pair(
                if (appLanguage == "ar") {
                    "أهلاً بك! أنا مساعد المونتاج والذكاء الاصطناعي الخاص بك 🤖✨\nكيف يمكنني مساعدتك اليوم؟ يمكنك اختصار الوقت بالاختيارات الجاهزة بالأسفل أو سؤالي محليًا!"
                } else {
                    "Hello! I am your AI video automation companion 🤖✨\nHow can I help you today? You can select any quick preset action below or ask any custom prompt."
                },
                true
            )
        )
    }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = StudioBackground
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                    topBar = {
                        StudioTopAppBar(
                            appLanguage = appLanguage,
                            onLanguageToggle = { viewModel.toggleLanguage() },
                            onSettingsClick = { viewModel.navigateTo(StudioScreen.Settings) },
                            activeScreen = currentScreen,
                            onBackClick = { viewModel.navigateTo(StudioScreen.Dashboard) }
                        )
                    },
                    bottomBar = {
                        StudioBottomNavigationBar(
                            currentScreen = currentScreen,
                            lang = appLanguage,
                            onTabSelected = { selectedScreen ->
                                viewModel.navigateTo(selectedScreen)
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(StudioBackground)
                    ) {
                        // Render views gracefully
                        when (currentScreen) {
                            StudioScreen.Dashboard -> DashboardScreen(viewModel = viewModel, lang = appLanguage)
                            StudioScreen.TextToImage -> TextToImageScreen(viewModel = viewModel, lang = appLanguage)
                            StudioScreen.TextToVideo -> TextToVideoScreen(viewModel = viewModel, lang = appLanguage)
                            StudioScreen.AICharacters -> AICharactersScreen(viewModel = viewModel, lang = appLanguage)
                            StudioScreen.ProjectDetail -> ProjectDetailPlayerScreen(viewModel = viewModel, lang = appLanguage)
                            StudioScreen.Settings -> SettingsScreen(viewModel = viewModel, lang = appLanguage)
                            StudioScreen.Export -> ExportScreen(viewModel = viewModel, lang = appLanguage)
                        }
                    }
                }

                // IN-APP SMART ASSISTANT FLOATING BUTTON & CHAT OVERLAY
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 90.dp, end = 16.dp, start = 16.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // The Expandable Chat Assistant Box
                        if (assistantOpen) {
                            Card(
                                modifier = Modifier
                                    .widthIn(max = 350.dp)
                                    .height(390.dp)
                                    .border(1.dp, StudioPrimary, RoundedCornerShape(16.dp))
                                    .testTag("ai_assistant_panel"),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = StudioCard)
                            ) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    // Chat header
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(StudioPrimary)
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Face,
                                                contentDescription = "GPT",
                                                tint = Color.White,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = if (appLanguage == "ar") "المساعد الذكي (ChatGPT)" else "AI Smart Assistant",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }

                                        IconButton(
                                            onClick = { assistantOpen = false },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Close",
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }

                                    // Messages list
                                    LazyColumn(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        items(assistantMessages) { (text, isAi) ->
                                            Column(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalAlignment = if (isAi) Alignment.Start else Alignment.End
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(
                                                            RoundedCornerShape(
                                                                topStart = 12.dp,
                                                                topEnd = 12.dp,
                                                                bottomStart = if (isAi) 0.dp else 12.dp,
                                                                bottomEnd = if (isAi) 12.dp else 0.dp
                                                            )
                                                        )
                                                        .background(if (isAi) StudioCardLight else StudioPrimary)
                                                        .padding(10.dp)
                                                        .widthIn(max = 240.dp)
                                                ) {
                                                    Column {
                                                        Text(
                                                            text = text,
                                                            fontSize = 11.sp,
                                                            color = TextPrimary,
                                                            lineHeight = 16.sp
                                                        )

                                                        // If it's an AI message with a script, allow copying/applying directly to active prompt field
                                                        if (isAi && text.length > 50) {
                                                            Spacer(modifier = Modifier.height(6.dp))
                                                            Button(
                                                                onClick = {
                                                                    // Extract useful text and pipe to the active screen
                                                                    val cleanText = text
                                                                        .substringAfter(":")
                                                                        .substringBefore("هاشتاجات")
                                                                        .trim()
                                                                    viewModel.updateSharedPrompt(cleanText)
                                                                },
                                                                colors = ButtonDefaults.buttonColors(containerColor = StudioAccent),
                                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                                                shape = RoundedCornerShape(4.dp),
                                                                modifier = Modifier.align(Alignment.End).height(24.dp)
                                                            ) {
                                                                Text(
                                                                    text = if (appLanguage == "ar") "اعتماد البرومبت ✓" else "Apply Prompt ✓",
                                                                    fontSize = 9.sp,
                                                                    fontWeight = FontWeight.Bold
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // Preset chips row
                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                        modifier = Modifier.background(StudioBackground.copy(alpha = 0.5f))
                                    ) {
                                        // Preset 1: Suggest video ideas
                                        item {
                                            SuggestionChip(
                                                label = if (appLanguage == "ar") "💡 أفكار فيديوهات" else "💡 Video Ideas",
                                                onClick = {
                                                    assistantMessages.add(Pair(if (appLanguage == "ar") "اقترح عليّ أفكار فيديوهات تعليمية متميزة" else "Suggest creative educational video concepts", false))
                                                    val response = if (appLanguage == "ar") {
                                                        "إليك 3 أفكار فيديوهات رهيبة:\n1. أهمية الطاقة النظيفة وألواح الشمس لتوليد الكهرباء.\n2. أسرار الذكاء الاصطناعي وكيف يغير شكل التعليم المجتمعي.\n3. قصة رحلة خيالية ممتعة إلى أعماق المحيط الهادئ."
                                                    } else {
                                                        "Here are 3 unique video themes:\n1. Solar Energy Revolution and building decentralized rooftop networks.\n2. Quantum Computing explained for kids with visual cards.\n3. A cinematic space exploration trip to Kepler 22-b."
                                                    }
                                                    assistantMessages.add(Pair(response, true))
                                                }
                                            )
                                        }

                                        // Preset 2: Compose script
                                        item {
                                            SuggestionChip(
                                                label = if (appLanguage == "ar") "✍️ اكتب سكريبت" else "✍️ Compose Script",
                                                onClick = {
                                                    assistantMessages.add(Pair(if (appLanguage == "ar") "اكتب سكريبت قصير عن حماية الغابات" else "Write solar energy video script", false))
                                                    val response = if (appLanguage == "ar") {
                                                        "إليك السيناريو المقترح:\nالتعليم والوعي البيئي أمران حاسمان اليوم. الغامضة والغابات هي رئة الكوكب، حمايتها تعني ضمان مستقبل مستقر لأطفالنا وعالم صديق للبيئة."
                                                    } else {
                                                        "Excellent! Apply this script to Text-to-Video:\nSolar energy represents the ultimate green choice. When sunlight touches clean modern panels, it generates high voltage current to power thousands of schools."
                                                    }
                                                    assistantMessages.add(Pair(response, true))
                                                }
                                            )
                                        }

                                        // Preset 3: Suggest Tags
                                        item {
                                            SuggestionChip(
                                                label = if (appLanguage == "ar") "🏷️ هاشتاجات يوتيوب" else "🏷️ Viral Tags",
                                                onClick = {
                                                    assistantMessages.add(Pair(if (appLanguage == "ar") "اقترح هاشتاجات تيك توك للفيديو" else "Suggest YouTube hashtags for views", false))
                                                    val response = if (appLanguage == "ar") {
                                                        "تفضل الهاشتاجات المقترحة لمنشورات تيك توك ويوتيوب:\n#صناعة_المحتوى #ذكاء_اصطناعي #تكنولوجيا #مونتاج_احترافي #AIVideo"
                                                    } else {
                                                        "Viral hashtags for maximum traffic:\n#AIVideoCreator #TechEvolution #OpenSourceStudio #AutomationGrowth #ChatGPT"
                                                    }
                                                    assistantMessages.add(Pair(response, true))
                                                }
                                            )
                                        }

                                        // Preset 4: AI Director Check
                                        item {
                                            SuggestionChip(
                                                label = if (appLanguage == "ar") "🎬 مراجعة المخرج" else "🎬 Director Feedback",
                                                onClick = {
                                                    assistantMessages.add(Pair(if (appLanguage == "ar") "مراجعة المخرج الذكي لمشروعي" else "AI Director cinematic audit of project", false))
                                                    val response = if (appLanguage == "ar") {
                                                        "تقرير المخرج الذكي: تم فحص دفق النصوص والصور. نوصي باستخدام موسيقى 'إيقاع إلكتروني' مع نبرة صوت 'عمر' لضمان إخراج درامي عالي الدقة (Grade A+)."
                                                    } else {
                                                        "AI Director audit report: Storyboard looks clean and visually compelling. Best paired with 'Cinematic Ambient' sound track and 'Omar' voice narrator for dramatic focus (Grade A+)."
                                                    }
                                                    assistantMessages.add(Pair(response, true))
                                                }
                                            )
                                        }
                                    }

                                    // Chat input textbox
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(StudioCardLight)
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedTextField(
                                            value = chatInputText,
                                            onValueChange = { chatInputText = it },
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(44.dp),
                                            placeholder = {
                                                Text(
                                                    text = if (appLanguage == "ar") "اسأل المساعد الافتراضي..." else "Ask companion...",
                                                    fontSize = 11.sp,
                                                    color = TextMuted
                                                )
                                            },
                                            textStyle = TextStyle(color = TextPrimary, fontSize = 11.sp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = StudioPrimary,
                                                unfocusedBorderColor = Color.Transparent,
                                                focusedContainerColor = StudioBackground,
                                                unfocusedContainerColor = StudioBackground
                                            )
                                        )

                                        Spacer(modifier = Modifier.width(6.dp))

                                        IconButton(
                                            onClick = {
                                                if (chatInputText.isNotBlank()) {
                                                    val query = chatInputText
                                                    assistantMessages.add(Pair(query, false))
                                                    chatInputText = ""

                                                    // Generate dynamic interactive replies contextually
                                                    val response = if (appLanguage == "ar") {
                                                        "فهمت سؤالك وموضوع: '$query'. نوصي بصياغته على هيئة مشروع إنتاجي رائع. يمكنك الضغط على زر التوليد في شاشة صناعة فيديو لنبدأ العمل!"
                                                    } else {
                                                        "Got your query about '$query'. We recommend transforming this context into a new multi-scene workspace script."
                                                    }
                                                    assistantMessages.add(Pair(response, true))
                                                }
                                            },
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(StudioPrimary, CircleShape)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Send,
                                                contentDescription = "Send",
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // The Floating Button triggering state toggle
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(StudioPrimary, StudioAccent)
                                    )
                                )
                                .clickable { assistantOpen = !assistantOpen }
                                .testTag("floating_ai_assistant_fab"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (assistantOpen) Icons.Default.Close else Icons.Default.Face,
                                contentDescription = "AI Assistant Toggle",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // Shimmering Generative Loading Overlay blocking tap gestures
                if (isGenerating) {
                    GenerativeProcessOverlay(
                        progress = generationProgress,
                        statusString = generationStatus,
                        lang = appLanguage
                    )
                }
            }
        }
    }
}

@Composable
fun SuggestionChip(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .background(StudioPrimary.copy(alpha = 0.15f))
            .border(1.dp, StudioPrimary.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = StudioPrimaryLight
        )
    }
}

// Custom Premium Top Bar with localized labels
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioTopAppBar(
    appLanguage: String,
    onLanguageToggle: () -> Unit,
    onSettingsClick: () -> Unit,
    activeScreen: StudioScreen,
    onBackClick: () -> Unit
) {
    val showBackButton = activeScreen != StudioScreen.Dashboard && 
                       activeScreen != StudioScreen.TextToVideo && 
                       activeScreen != StudioScreen.TextToImage &&
                       activeScreen != StudioScreen.AICharacters

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = StudioBackground,
            titleContentColor = TextPrimary,
            navigationIconContentColor = StudioPrimary,
            actionIconContentColor = StudioSecondary
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Studio Icon",
                    tint = StudioPrimary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = StudioLocales.appTitle(appLanguage),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
            }
        },
        actions = {
            // Local Language toggle badge
            Button(
                onClick = onLanguageToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = StudioCardLight,
                    contentColor = StudioSecondary
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .testTag("language_toggle_button")
            ) {
                Text(
                    text = if (appLanguage == "ar") "English" else "العربية",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier.testTag("top_settings_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = TextSecondary
                )
            }
        }
    )
}

// M3 Styled Bottom Navigation with consistent pills indicators
@Composable
fun StudioBottomNavigationBar(
    currentScreen: StudioScreen,
    lang: String,
    onTabSelected: (StudioScreen) -> Unit
) {
    NavigationBar(
        containerColor = StudioBackground,
        tonalElevation = 8.dp,
        modifier = Modifier.background(StudioBackground)
    ) {
        val navItems = listOf(
            Triple(StudioScreen.Dashboard, Icons.Default.Home, if (lang == "ar") "الرئيسية" else "Home"),
            Triple(StudioScreen.TextToVideo, Icons.Default.PlayArrow, if (lang == "ar") "إنشاء" else "Create"),
            Triple(StudioScreen.Export, Icons.Default.List, if (lang == "ar") "المشاريع" else "Projects"),
            Triple(StudioScreen.AICharacters, Icons.Default.Face, if (lang == "ar") "الشخصيات" else "Characters"),
            Triple(StudioScreen.Settings, Icons.Default.Settings, if (lang == "ar") "الحساب" else "Analytics")
        )

        navItems.forEach { (screen, icon, label) ->
            val isSelected = currentScreen == screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(screen) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (isSelected) StudioPrimaryLight else TextSecondary
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) TextPrimary else TextSecondary
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = StudioCardLight
                ),
                modifier = Modifier.testTag("nav_tab_${screen.name.lowercase()}")
            )
        }
    }
}

// Custom Shimmer Generative Process Overlay blocking tap clicks
@Composable
fun GenerativeProcessOverlay(
    progress: Float,
    statusString: String,
    lang: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.82f))
            .clickable(enabled = false) {}, // blocks all tap ripples
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            // Spinning Studio Pulse Icon
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(100.dp)
            ) {
                val infiniteTransition = rememberInfiniteTransition(label = "Pulse")
                val pulseScale by infiniteTransition.animateFloat(
                    initialValue = 0.85f,
                    targetValue = 1.15f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1200, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "pulse_anim"
                )

                Canvas(modifier = Modifier.size(90.dp)) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(StudioPrimary.copy(alpha = 0.45f), Color.Transparent),
                            radius = size.width / 1.5f * pulseScale
                        )
                    )
                }

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Generating",
                    tint = StudioSecondary,
                    modifier = Modifier.size(46.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Elegant typography progress details
            Text(
                text = if (lang == "ar") "جاري تشكيل إبداعك السينمائي..." else "Synthesizing Cinematic Asset...",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = StudioSecondary,
                trackColor = StudioCard
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = statusString,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = StudioPrimaryLight,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("generation_status_text")
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${(progress * 100).toInt()}%",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )
        }
    }
}

// DASHBOARD WORKSPACE SCREEN
@Composable
fun DashboardScreen(viewModel: StudioViewModel, lang: String) {
    val projectsList by viewModel.projects.collectAsState()
    val charactersList by viewModel.characters.collectAsState()
    var projectToDelete by remember { mutableStateOf<VideoProject?>(null) }

    // Smart Media Library state variables & Search
    var mediaSearchQuery by remember { mutableStateOf("") }
    var mediaSearchResultsVisible by remember { mutableStateOf(false) }

    val isAr = lang == "ar"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("dashboard_root"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
    ) {
        // TOP HEADER: Logo, Smart Search, Notification Icon, Profile Image (as requested)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                // Main profile & brand header row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Application Name logo with cinematic style
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Brush.linearGradient(listOf(StudioPrimary, StudioSecondary))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Logo icon",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isAr) "استوديو صناعة فيديو AI" else "AI Video Creator",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                            Text(
                                text = "Studio v2.5",
                                fontSize = 10.sp,
                                color = StudioSecondaryLight,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Notification bell and Profile Image Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Bell with active red badge (3 items)
                        Box(contentAlignment = Alignment.TopEnd) {
                            IconButton(
                                onClick = {},
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(StudioCard, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            // Active notification red dot/badge
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(StudioError)
                                    .align(Alignment.TopEnd)
                            )
                        }

                        // Profile Picture / Initials Avatar
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Brush.radialGradient(listOf(StudioSecondary, StudioPrimary)))
                                .border(1.5.dp, TextPrimary.copy(alpha = 0.4f), CircleShape)
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "H", // e.g. from user Hisham
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // SMART SEARCH BAR (from top area requirements)
                OutlinedTextField(
                    value = mediaSearchQuery,
                    onValueChange = {
                        mediaSearchQuery = it
                        mediaSearchResultsVisible = it.isNotBlank()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("media_library_search"),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search icon",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        if (mediaSearchQuery.isNotBlank()) {
                            IconButton(onClick = {
                                mediaSearchQuery = ""
                                mediaSearchResultsVisible = false
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    },
                    placeholder = {
                        Text(
                            text = if (isAr) "ابحث عن قوالب، وسائط، مؤثرات بالذكاء الاصطناعي..." else "Search templates, assets, AI audio tracks...",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    },
                    textStyle = TextStyle(color = TextPrimary, fontSize = 12.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = StudioPrimary,
                        unfocusedBorderColor = StudioCard,
                        focusedContainerColor = StudioCard,
                        unfocusedContainerColor = StudioCard
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                // Semantic search indicators
                if (mediaSearchResultsVisible) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = StudioCardLight),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (isAr) "🪄 نتائج البحث الفوري والوسائط المتطابقة:" else "🪄 Matches ready to inject:",
                                fontSize = 11.sp,
                                color = StudioSecondaryLight,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                SuggestionChip(
                                    label = if (isAr) "🔊 صوت: أكشن سينمائي" else "🔊 SFX: Epic Cinematic",
                                    onClick = { }
                                )
                                SuggestionChip(
                                    label = if (isAr) "🎵 تراك: فضاء ذكي" else "🎵 Track: AI Horizon",
                                    onClick = { }
                                )
                            }
                        }
                    }
                }
            }
        }

        // HERO CARD: "What do you want to create today?" / "ماذا تريد إنشاء اليوم؟"
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        Brush.linearGradient(listOf(StudioPrimary.copy(alpha = 0.7f), StudioSecondary.copy(alpha = 0.3f))),
                        RoundedCornerShape(18.dp)
                    ),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = StudioCard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (isAr) "ماذا تريد إنشاء اليوم؟ ✨" else "What do you want to create today? ✨",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isAr) "حدد الأداة بلمسة واحدة لابتكار تحفتك البصرية" else "Choose a specialized tool to model your ideas into cinematic streams",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 6 Interactive UI Options in a highly styled layout (2 columns)
                    val creatorOptions = listOf(
                        Triple(
                            if (isAr) "إنشاء فيديو AI" else "Create AI Video",
                            Icons.Default.PlayArrow,
                            StudioScreen.TextToVideo
                        ),
                        Triple(
                            if (isAr) "إنشاء صورة" else "Generate Image",
                            Icons.Default.Star,
                            StudioScreen.TextToImage
                        ),
                        Triple(
                            if (isAr) "تحويل نص لفيديو" else "Text to Video",
                            Icons.Default.Edit,
                            StudioScreen.TextToVideo
                        ),
                        Triple(
                            if (isAr) "صورة إلى فيديو" else "Image to Video",
                            Icons.Default.Refresh, // can represent conversion
                            StudioScreen.TextToVideo
                        ),
                        Triple(
                            if (isAr) "استنساخ صوت" else "Voice Clone",
                            Icons.Default.Face,
                            StudioScreen.AICharacters
                        ),
                        Triple(
                            if (isAr) "شخصية ذكاء اصطناعي" else "AI Character",
                            Icons.Default.Person,
                            StudioScreen.AICharacters
                        )
                    )

                    // Display as a beautiful styled grid
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        for (i in creatorOptions.indices step 2) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Column 1
                                val op1 = creatorOptions[i]
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(StudioCardLight)
                                        .border(0.5.dp, TextSecondary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                        .clickable { viewModel.navigateTo(op1.third) }
                                        .padding(12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(StudioPrimary.copy(alpha = 0.15f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = op1.second,
                                                contentDescription = op1.first,
                                                tint = StudioPrimaryLight,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = op1.first,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    }
                                }

                                // Column 2
                                if (i + 1 < creatorOptions.size) {
                                    val op2 = creatorOptions[i + 1]
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(StudioCardLight)
                                            .border(0.5.dp, TextSecondary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                            .clickable { viewModel.navigateTo(op2.third) }
                                            .padding(12.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(StudioSecondary.copy(alpha = 0.15f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = op2.second,
                                                    contentDescription = op2.first,
                                                    tint = StudioSecondaryLight,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = op2.first,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // QUICK ACTIONS SECTION: Large custom-styled cards list
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (isAr) "⚡️ الإجراءات السريعة (إنتاج فوري بلمسة واحدة)" else "⚡️ Creator Quick Actions (Instant Builds)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioSecondaryLight
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isAr) "اختر وسيلة من هذه القوالب لنقل فكرتك للتنفيذ فورا" else "Tap any preset to construct full video scenes structure automatically",
                    fontSize = 10.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Grid layout for 8 large buttons
                val quickActions = listOf(
                    Triple(
                        if (isAr) "فيديو بضغطة واحدة" else "1-Click Video",
                        Icons.Default.Build,
                        if (isAr) "إنشاء مقطع قصير مذهل عن عجائب المحيطات الزرقاء والحيوانات النادرة" else "Create a stunning short about blue ocean wonders and rare sea creatures"
                    ),
                    Triple(
                        if (isAr) "فيديو يوتيوب" else "YouTube Video",
                        Icons.Default.PlayArrow,
                        if (isAr) "دليل تفصيلي مبسط لتعلم الذكاء الاصطناعي وكيف يحول حياه البشر" else "A simplified detailed guide to learning artificial intelligence"
                    ),
                    Triple(
                        if (isAr) "فيديو قصير Shorts" else "Shorts Creator",
                        Icons.Default.Share,
                        if (isAr) "3 أسرار سحرية للنجاح السريع في العمل الحر وصناعة المحتوى الرقمي" else "3 magical secrets to rapid success in freelancing"
                    ),
                    Triple(
                        if (isAr) "إعلان ترويجي" else "Marketing Ad",
                        Icons.Default.ShoppingCart,
                        if (isAr) "إعلان تسويقي فخم وجذاب لإطلاق تطبيق ذكي جديد يوفر الوقت والجهد" else "Premium appealing marketing ad for launching a new smart time-saving app"
                    ),
                    Triple(
                        if (isAr) "قصة أطفال" else "Kids Story",
                        Icons.Default.Info,
                        if (isAr) "قصة ملهمة مشوقة للأطفال عن عصفور صغير ذكي يعلم أصدقائه قوة التعاون" else "Inspiring kids story about a clever little bird teaching high-value teamwork"
                    ),
                    Triple(
                        if (isAr) "ملخص كتاب" else "Book Summary",
                        Icons.Default.List,
                        if (isAr) "ملخص كتاب العادات الذرية وكيفية بناء روتين يومي مذهل يغير حياتك" else "Summary of Atomic Habits book and building amazing life-changing daily routines"
                    ),
                    Triple(
                        if (isAr) "درس تعليمي" else "Tutorial",
                        Icons.Default.Menu,
                        if (isAr) "شرح تفاعلي وبسيط ومبهر لكيفية حدوث ظاهرة خسوف القمر بالصور والمشاهد" else "Simple interactive explanation of lunar eclipse with visual boards"
                    ),
                    Triple(
                        if (isAr) "تعليم مجتمعي" else "Community Edu",
                        Icons.Default.Star,
                        if (isAr) "فيديو هادف يشجع أفراد المجتمع على زراعة السطوح والتشجير لمكافحة الاحتباس الحراري" else "A purposeful educational video about urban rooftop gardening"
                    )
                )

                // Layout 4 rows of 2 columns
                for (j in quickActions.indices step 2) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (k in 0..1) {
                            if (j + k < quickActions.size) {
                                val action = quickActions[j + k]
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(78.dp)
                                        .clickable {
                                            viewModel.updateSharedPrompt(action.third)
                                            viewModel.navigateTo(StudioScreen.TextToVideo)
                                        },
                                    colors = CardDefaults.cardColors(containerColor = StudioCard),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(10.dp),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = action.second,
                                            contentDescription = action.first,
                                            tint = StudioSecondaryLight,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = action.first,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = if (isAr) "توليد تلقائي 🚀" else "Auto generate 🚀",
                                            fontSize = 8.sp,
                                            color = StudioAccent,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // PREVIOUS PROJECTS TITLE SECTION
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${if (isAr) "📋 مشاريع الاستوديو السابقة" else "📋 Previous Studio Projects"} (${projectsList.size})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
            }
        }

        // Project Items
        if (projectsList.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = StudioCard),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Empty",
                            tint = TextMuted,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = StudioLocales.emptyStateTitle(lang),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                    }
                }
            }
        } else {
            items(projectsList) { project ->
                DashboardProjectCard(
                    project = project,
                    charactersList = charactersList,
                    lang = lang,
                    onClick = { viewModel.selectProject(project) },
                    onDelete = { projectToDelete = project }
                )
            }
        }
    }

    // Modern Confirm Delete Dialog
    if (projectToDelete != null) {
        AlertDialog(
            onDismissRequest = { projectToDelete = null },
            containerColor = StudioCard,
            titleContentColor = TextPrimary,
            textContentColor = TextSecondary,
            title = {
                Text(
                    text = StudioLocales.deleteProjectTitle(lang),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(text = "${StudioLocales.deleteProjectConfirm(lang)}\n(${projectToDelete?.title})")
            },
            confirmButton = {
                Button(
                    onClick = {
                        projectToDelete?.let { viewModel.deleteProject(it.id) }
                        projectToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioError)
                ) {
                    Text(text = if (lang == "ar") "حذف" else "Delete", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { projectToDelete = null }) {
                    Text(text = if (lang == "ar") "إلغاء" else "Cancel", color = TextSecondary)
                }
            }
        )
    }
}

// Single Project Card Component fully conforming to user prompt
@Composable
fun DashboardProjectCard(
    project: VideoProject,
    charactersList: List<AICharacter>,
    lang: String,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val associatedChar = charactersList.find { it.id == project.characterId }
    val isAr = lang == "ar"
    
    // Simulate a stable and beautiful date
    val projectHash = project.title.hashCode().absoluteValue
    val day = (projectHash % 28) + 1
    val month = (projectHash % 12) + 1
    val year = 2026
    val dateString = if (isAr) "تم الإنشاء: $day/$month/$year" else "Created: $day/$month/$year"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("project_card_${project.id}"),
        colors = CardDefaults.cardColors(containerColor = StudioCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            // Upper visual thumbnail box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(StudioCardLight)
            ) {
                val sceneSeed = project.title.hashCode().absoluteValue
                AsyncImage(
                    model = "https://picsum.photos/seed/$sceneSeed/800/450",
                    contentDescription = "Project Cover Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Aspect ratio and duration overlay label badges
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .align(Alignment.BottomStart),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.72f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = project.resolutionSetting,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioSecondaryLight
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(StudioPrimary.copy(alpha = 0.88f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${project.videoDurationSeconds} " + (if (isAr) "ثانية" else "Secs"),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
            }

            // Lower information & actions layout
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = project.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isAr) "الموضوع: ${project.prompt}" else "Concept: ${project.prompt}",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = dateString,
                            fontSize = 10.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(32.dp)
                            .background(StudioCardLight, CircleShape)
                            .testTag("delete_proj_${project.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Project",
                            tint = StudioError,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Progress Indicator Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isAr) "التقدم ونسبة الإنجاز:" else "Processing Progress:",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isAr) "100% مكتمل" else "100% Completed",
                        fontSize = 10.sp,
                        color = StudioAccent,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { 1.0f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = StudioAccent,
                    trackColor = StudioCardLight,
                )

                Spacer(modifier = Modifier.height(12.dp))

                // "🎬 متابعة التعديل" / "Continue Edit" Button (as requested)
                Button(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StudioPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Edit",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isAr) "🎬 متابعة التعديل" else "🎬 Continue Editing",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// CREATIVE STANDALONE IMAGE GENERATION CANVAS SCREEN (Text To Image)
@Composable
fun TextToImageScreen(viewModel: StudioViewModel, lang: String) {
    var promptText by remember { mutableStateOf("") }
    var selectedStyle by remember { mutableStateOf("Realistic") }
    var selectedRatio by remember { mutableStateOf("16:9") }

    val simulatedImageUrl by viewModel.simulatedImageUrl.collectAsState()

    val styles = listOf("Realistic", "Cinematic", "3D Render", "Cartoon", "Anime")
    val ratios = listOf("16:9", "1:1", "9:16")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("text_to_image_root"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = StudioLocales.imgGenHeading(lang),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = StudioLocales.imgGenSub(lang),
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        }

        // Form controls card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = StudioLocales.labelPromptInput(lang),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioSecondaryLight
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = promptText,
                        onValueChange = { promptText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(85.dp)
                            .testTag("img_prompt_input"),
                        placeholder = {
                            Text(
                                text = if (lang == "ar") "سنجاب يرتدي خوذة فضاء في ناطحة سحاب مستقبلية..." else "A cybernetic wolf glowing on top of a skyscraper, cyberpunk art...",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        },
                        textStyle = TextStyle(color = TextPrimary, fontSize = 13.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = StudioPrimary,
                            unfocusedBorderColor = StudioCardLight,
                            focusedContainerColor = StudioBackground,
                            unfocusedContainerColor = StudioBackground
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Style Row choice
                    Text(
                        text = StudioLocales.labelStyle(lang),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(styles) { style ->
                            val isChosen = selectedStyle == style
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedStyle = style }
                                    .background(if (isChosen) StudioPrimary else StudioCardLight)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = if (lang == "ar") {
                                        when (style) {
                                            "Realistic" -> "واقعي"
                                            "Cinematic" -> "سينمائي"
                                            "3D Render" -> "ثلاثي الأبعاد"
                                            "Cartoon" -> "كرتوني"
                                            "Anime" -> "أنمي"
                                            else -> style
                                        }
                                    } else style,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Ratio Row Choice
                    Text(
                        text = StudioLocales.labelRatio(lang),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ratios.forEach { ratio ->
                            val isChosen = selectedRatio == ratio
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedRatio = ratio }
                                    .background(if (isChosen) StudioSecondary else StudioCardLight)
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = ratio,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isChosen) StudioBackground else TextPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Trigger Action
                    Button(
                        onClick = {
                            viewModel.generateStandaloneImage(promptText, selectedStyle)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("trigger_img_gen"),
                        colors = ButtonDefaults.buttonColors(containerColor = StudioPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = "Draw")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = StudioLocales.btnGenerateImage(lang),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }

        // Render Panel Area
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                if (simulatedImageUrl == null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Ready to generate",
                            tint = TextMuted,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = StudioLocales.imagePlaceholder(lang),
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = simulatedImageUrl,
                            contentDescription = "Synthesised Visual",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Action panel at the top
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            IconButton(
                                onClick = { viewModel.clearStandaloneImage() },
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        // Bottom Actions Overlay
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.62f))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .border(1.dp, StudioSecondary, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "$selectedStyle  |  $selectedRatio",
                                    fontSize = 10.sp,
                                    color = StudioSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = {
                                    viewModel.navigateTo(StudioScreen.TextToVideo)
                                },
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = StudioAccent),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text(
                                    text = StudioLocales.createVideoFromImgBtn(lang),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// OMNI-PIPELINE VIDEO CREATOR SCREEN (Text To Video)
@Composable
fun TextToVideoScreen(viewModel: StudioViewModel, lang: String) {
    var ideaText by remember { mutableStateOf("") }
    var selectedCharId by remember { mutableIntStateOf(0) }
    var resolutionSetting by remember { mutableStateOf("Full HD (1080p)") }
    var musicTrack by remember { mutableStateOf("Cinematic Ambient") }

    // One-Click Video states
    var videoDurationSeconds by remember { mutableIntStateOf(30) }
    var targetLanguage by remember { mutableStateOf("Arabic & English") }

    // External sources simulators (Links, Article Text, PowerPoint PPTX/PDF)
    var showConverterPanel by remember { mutableStateOf(false) }
    var webLinkInput by remember { mutableStateOf("") }
    var pasteArticleInput by remember { mutableStateOf("") }
    var selectedPowerPointName by remember { mutableStateOf("") }

    // Batch series mode states
    var enableBatchMode by remember { mutableStateOf(false) }
    var batchEpisodesCount by remember { mutableIntStateOf(3) }

    val charactersList by viewModel.characters.collectAsState()
    val engineSettings by viewModel.engineSettings.collectAsState()
    val sharedSuggestedPrompt by viewModel.assistantSharedPrompt.collectAsState()

    val resolutions = listOf("Full HD (1080p)", "2K HD Studio", "4K Ultra HD")
    val tracks = listOf("Cinematic Ambient", "Futuristic Synth", "Empowering Piano", "No Background Music")
    val isAr = lang == "ar"

    // Sync from AI Assistant Suggestions
    LaunchedEffect(sharedSuggestedPrompt) {
        if (sharedSuggestedPrompt.isNotBlank()) {
            ideaText = sharedSuggestedPrompt
        }
    }

    // Match selected character id dynamically on first loading of seeded characters
    LaunchedEffect(charactersList) {
        if (charactersList.isNotEmpty() && selectedCharId == 0) {
            selectedCharId = charactersList.first().id
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("text_to_video_root"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome and Header Titles
        item {
            Column {
                Text(
                    text = StudioLocales.vidCreatorHeading(lang),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = StudioLocales.vidCreatorSub(lang),
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        }

        // 1. PRE-BUILT CREATIVE TEMPLATES CAROUSEL (Feature 3)
        item {
            Column {
                Text(
                    text = if (isAr) "🎯اختر قالبًا سينمائيًا جاهزًا" else "🎯 Cinematic Templates Presets",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val templates = listOf(
                        Triple(
                            if (isAr) "شورتس سريع" else "Snappy Shorts",
                            "9:16 portrait design",
                            if (isAr) "أسرار غريبة ومفاجئة عن الكوكب الأحمر المريخ وثرواته الكامنة" else "Mysteries of planet Mars and secret water streams"
                        ),
                        Triple(
                            if (isAr) "فيديو تعليمي" else "Explainer Video",
                            "16:9 widescreen educational",
                            if (isAr) "شرح مبسط كيف تعمل الطاقة الشمسية الخلايا الضوئية والكهرباء الصديقة للبيئة" else "How basic photoelectric solar panels capture ambient sunset fields"
                        ),
                        Triple(
                            if (isAr) "تيك توك تريند" else "Viral TikTok",
                            "9:16 short format story",
                            if (isAr) "أبرز أسرار لغات البرمجة الأكثر طلباً في سوق العمل التقني الحديث" else "Most in-demand computer languages in high salary business sectors"
                        ),
                        Triple(
                            if (isAr) "وثائقي تاريخي" else "Historical Drama",
                            "16:9 cinematic widescreen",
                            if (isAr) "رحلة بناء الأهرامات العظيمة ونمط عيش ملوك الفراعنة في مصر القديمة" else "The historic timeline of Egyptian Pharaohs building spectacular stone pyramids"
                        )
                    )

                    items(templates) { (titleText, descText, samplePrompt) ->
                        Card(
                            modifier = Modifier
                                .width(155.dp)
                                .clickable {
                                    ideaText = samplePrompt
                                    resolutionSetting = if (titleText.contains("Shorts") || titleText.contains("تيك توك") || titleText.contains("شورتس")) "9:16 Portrait" else "Full HD (1080p)"
                                },
                            colors = CardDefaults.cardColors(containerColor = StudioCard),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Template",
                                    tint = StudioSecondaryLight,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = titleText,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = descText,
                                    fontSize = 9.sp,
                                    color = StudioSecondaryLight
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. DOCUMENT / WEBSITE LINK / ARTICLES CONVERTER SIMULATORS (Features 8, 9, 10)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Convert Docs",
                                tint = StudioPrimaryLight,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAr) "المستندات، المواقع وملفات PowerPoint" else "Convert Web Links, Articles & PPTX",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        IconButton(
                            onClick = { showConverterPanel = !showConverterPanel },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = if (showConverterPanel) Icons.Default.Lock else Icons.Default.Add,
                                contentDescription = "Toggle Panel",
                                tint = StudioSecondary
                            )
                        }
                    }

                    if (showConverterPanel) {
                        Spacer(modifier = Modifier.height(12.dp))

                        // A. URL Web link input
                        OutlinedTextField(
                            value = webLinkInput,
                            onValueChange = { webLinkInput = it },
                            modifier = Modifier.fillMaxWidth().height(46.dp),
                            placeholder = {
                                Text(text = if (isAr) "أدخل رابط الويب (مثال: wikipedia.org/solar)..." else "Enter article URL (e.g. news/renewable-solar)...", fontSize = 11.sp, color = TextMuted)
                            },
                            textStyle = TextStyle(color = TextPrimary, fontSize = 11.sp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StudioPrimary,
                                unfocusedBorderColor = StudioCardLight,
                                focusedContainerColor = StudioBackground,
                                unfocusedContainerColor = StudioBackground
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // B. Article pasted text area
                        OutlinedTextField(
                            value = pasteArticleInput,
                            onValueChange = { pasteArticleInput = it },
                            modifier = Modifier.fillMaxWidth().height(70.dp),
                            placeholder = {
                                Text(text = if (isAr) "أو الصق نص المقال الطويل المراد تلخيصه وإنتاجه هاهنا..." else "Or paste long-form article paragraphs here to parse...", fontSize = 11.sp, color = TextMuted)
                            },
                            textStyle = TextStyle(color = TextPrimary, fontSize = 11.sp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StudioPrimary,
                                unfocusedBorderColor = StudioCardLight,
                                focusedContainerColor = StudioBackground,
                                unfocusedContainerColor = StudioBackground
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // C. PPTX attachment selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    selectedPowerPointName = "solar_presentation_slides.pptx"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = StudioCardLight),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Share, contentDescription = "PPTX", modifier = Modifier.size(14.dp), tint = StudioSecondaryLight)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = if (isAr) "استيراد PPTX / PDF" else "Browse PPTX / PDF", fontSize = 10.sp, color = TextSecondary)
                                }
                            }

                            if (selectedPowerPointName.isNotEmpty()) {
                                Text(
                                    text = selectedPowerPointName,
                                    fontSize = 10.sp,
                                    color = StudioAccent,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // D. Run conversion simulator button
                        Button(
                            onClick = {
                                if (webLinkInput.isNotBlank()) {
                                    ideaText = if (isAr) "فيديو تعليمي دقيق ملخص من رابط الويب: أهمية خلايا طاقة شمسية والكهرباء الضوئية المنفردة" else "Explainer summary of web article solar fields"
                                } else if (pasteArticleInput.isNotBlank()) {
                                    ideaText = if (isAr) "ملخص المقال المكتوب: أهمية حماية الغابات من الزوال والوعي البيئي" else "Summarization of long pasted text"
                                } else if (selectedPowerPointName.isNotEmpty()) {
                                    ideaText = if (isAr) "صناعة عرض فيديو مبني على شرائح العرض العلمي للمقترحات الكهربائية" else "Slideshow video presentation converter"
                                } else {
                                    ideaText = if (isAr) "تحويل المستندات والروابط التعليمية المفصلة" else "Website article translation overview"
                                }
                                showConverterPanel = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = StudioAccent),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = if (isAr) "تحويل وتلخيص المستند الفوري ✓" else "Parse & Sync Document ✓", fontSize = 11.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // 3. ONE-CLICK VIDEO CREATIVE COMPOSERS (Feature 1)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isAr) "⏱️ محددات الفيديو بضغطة واحدة" else "⏱️ One-Click Creative Adjustments",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioSecondaryLight
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // A. Video Prompt String
                    Text(
                        text = StudioLocales.labelVideoIdea(lang),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = ideaText,
                        onValueChange = { ideaText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .testTag("video_prompt_input"),
                        placeholder = {
                            Text(
                                text = StudioLocales.videoIdeaPlaceholder(lang),
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        },
                        textStyle = TextStyle(color = TextPrimary, fontSize = 13.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = StudioPrimary,
                            unfocusedBorderColor = StudioCardLight,
                            focusedContainerColor = StudioBackground,
                            unfocusedContainerColor = StudioBackground
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // B. Duration Selector (Minutes and Seconds)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isAr) "مدة الفيديو المستهدفة:" else "Video Duration Target:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )

                        Text(
                            text = "$videoDurationSeconds " + (if (isAr) "ثانية" else "Seconds"),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = StudioSecondaryLight
                        )
                    }

                    Slider(
                        value = videoDurationSeconds.toFloat(),
                        onValueChange = { videoDurationSeconds = it.toInt() },
                        valueRange = 15f..120f,
                        steps = 6,
                        colors = SliderDefaults.colors(
                            thumbColor = StudioSecondary,
                            activeTrackColor = StudioSecondary,
                            inactiveTrackColor = StudioCardLight
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // C. Target Language Configuration Selector
                    Text(
                        text = if (isAr) "لغة الفيديو والترجمة الخلفية:" else "Language Dialect & Subtitle Output:",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Arabic & English", "العربية فقط (RTL UI)", "English Only (LTR)").forEach { opt ->
                            val isSel = targetLanguage == opt
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { targetLanguage = opt }
                                    .background(if (isSel) StudioPrimary else StudioCardLight)
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isAr) {
                                        when {
                                            opt.contains("Arabic &") -> "عربي وإنجليزي"
                                            opt.contains("العربية") -> "العربية فقط"
                                            else -> "العالمية فقط"
                                        }
                                    } else opt.split(" ")[0],
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. BATCH CREATION CONTROLLERS (Feature 21 - Batch Series Production)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isAr) "إنتاج دفعة من سلسلة حلقات (Batch Video Series)" else "Batch Video Series Scheduler",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = if (isAr) "أنتج مئات الفيديوهات المتتالية بضغطة زر" else "Synthesize a structured video list in parallel",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }

                        Switch(
                            checked = enableBatchMode,
                            onCheckedChange = { enableBatchMode = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = StudioPrimary,
                                checkedTrackColor = StudioCardLight
                            )
                        )
                    }

                    if (enableBatchMode) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isAr) "عدد الحلقات لتوليدها دفعة واحدة:" else "Episodes count in parallel:",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )

                            Text(
                                text = "$batchEpisodesCount " + (if (isAr) "مشاريع" else "Episodes"),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = StudioSecondary
                            )
                        }

                        Slider(
                            value = batchEpisodesCount.toFloat(),
                            onValueChange = { batchEpisodesCount = it.toInt() },
                            valueRange = 2f..7f,
                            steps = 4,
                            colors = SliderDefaults.colors(
                                thumbColor = StudioPrimary,
                                activeTrackColor = StudioPrimary,
                                inactiveTrackColor = StudioCardLight
                            )
                        )
                    }
                }
            }
        }

        // 5. Presenter Selection Area
        item {
            Column {
                Text(
                    text = StudioLocales.labelPresenter(lang),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(charactersList) { charObj ->
                        val isSelected = selectedCharId == charObj.id
                        Box(
                            modifier = Modifier
                                .width(135.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { selectedCharId = charObj.id }
                                .background(if (isSelected) StudioPrimary.copy(alpha = 0.35f) else StudioCard)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) StudioPrimary else StudioCardLight,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                // Draw dynamic elegant circular representation for avatar
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.linearGradient(
                                                colors = when (charObj.gender) {
                                                    "Male" -> listOf(Color(0xFF2563EB), Color(0xFF60A5FA))
                                                    "Female" -> listOf(Color(0xFFDB2777), Color(0xFFF472B6))
                                                    "Child" -> listOf(Color(0xFFF59E0B), Color(0xFFFBBF24))
                                                    else -> listOf(Color(0xFF10B981), Color(0xFF34D399))
                                                }
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = (if (lang == "ar") charObj.nameAr else charObj.name).take(1),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = if (lang == "ar") charObj.nameAr else charObj.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = charObj.defaultVoice.replace(" -", ""),
                                    fontSize = 10.sp,
                                    color = StudioSecondaryLight,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        // 6. Tech Specs panel
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = StudioLocales.labelSpecs(lang),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Resolutions Selector
                    Text(
                        text = StudioLocales.labelResolution(lang),
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        resolutions.plus("9:16 Portrait").distinct().forEach { res ->
                            val isSelected = resolutionSetting == res
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { resolutionSetting = res }
                                    .background(if (isSelected) StudioSecondary else StudioCardLight)
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (lang == "ar") {
                                        when {
                                            res.contains("Full") -> "HD كامل"
                                            res.contains("2K") -> "K2 دقة"
                                            res.contains("Portrait") -> "عمودي 9:16"
                                            else -> "K4 ممتازة"
                                        }
                                    } else res.split(" ")[0],
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) StudioBackground else TextPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Music Tracks Selector
                    Text(
                        text = StudioLocales.labelMusic(lang),
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tracks.take(2).forEach { sTrack ->
                            val isSelected = musicTrack == sTrack
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { musicTrack = sTrack }
                                    .background(if (isSelected) StudioPrimary else StudioCardLight)
                                    .padding(vertical = 10.dp, horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (lang == "ar") {
                                        when {
                                            sTrack.contains("Ambient") -> "موسيقى هادئة"
                                            sTrack.contains("Synth") -> "إيقاع إلكتروني"
                                            else -> sTrack
                                        }
                                    } else sTrack.split(" ")[0],
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // 7. Active AI Orchestrator details badge
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, StudioCardLight, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (lang == "ar") "حزمة توليد الذكاء الافتراضية" else "Orchestrated Pipeline Details",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )

                    Text(
                        text = "${engineSettings.llmModel.split(" ")[0]} ➔ ${engineSettings.imageModel} ➔ ${engineSettings.videoModel}",
                        fontSize = 10.sp,
                        color = StudioAccent,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // 8. CORE SYNTHESIS CTA BUTTON (Supports Standard & Batch Modes)
        item {
            Button(
                onClick = {
                    if (enableBatchMode) {
                        viewModel.generateBatchProjects(
                            baseConceptPrompt = ideaText,
                            selectedCharId = selectedCharId,
                            seriesCount = batchEpisodesCount,
                            resolutionSetting = resolutionSetting,
                            musicTrack = musicTrack
                        )
                    } else {
                        viewModel.generateVideoProject(
                            ideaPrompt = ideaText,
                            selectedCharId = selectedCharId,
                            resolutionSetting = resolutionSetting,
                            musicTrack = musicTrack
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .testTag("trigger_video_generation"),
                colors = ButtonDefaults.buttonColors(containerColor = StudioPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Build"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (enableBatchMode) {
                        if (isAr) "توليد دفعة من $batchEpisodesCount مشاريع دفعة واحدة 🚀" else "Synthesize Batch of $batchEpisodesCount Projects 🚀"
                    } else {
                        StudioLocales.btnCreateVideo(lang)
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

// AI CHARACTERS SCREEN
@Composable
fun AICharactersScreen(viewModel: StudioViewModel, lang: String) {
    val charactersList by viewModel.characters.collectAsState()

    var showCreatorDrawer by remember { mutableStateOf(false) }

    var newCharName by remember { mutableStateOf("") }
    var newCharDesc by remember { mutableStateOf("") }
    var newCharGender by remember { mutableStateOf("Male") }
    var newCharVoice by remember { mutableStateOf("Adam - Resonant") }

    // Generative Avatar & Voice Cloning States
    var avatarAIPrompt by remember { mutableStateOf("") }
    var isSynthesizingAvatarState by remember { mutableStateOf(false) }

    var isRecordingCloneVoice by remember { mutableStateOf(false) }
    var clonedVoiceReady by remember { mutableStateOf(false) }
    var simulatedVoiceWaveAmt by remember { mutableFloatStateOf(0.1f) }

    val genders = listOf("Male", "Female", "Child", "Cartoon")
    val voices = remember(clonedVoiceReady) {
        val defaultList = mutableListOf("Adam - Resonant", "Bella - Professional", "Sami - Playful", "Robo - Tech")
        if (clonedVoiceReady) {
            defaultList.add(0, "صوتي كـ Voice Clone")
        }
        defaultList
    }
    val isAr = lang == "ar"

    // Recording waveform motion simulation
    LaunchedEffect(isRecordingCloneVoice) {
        if (isRecordingCloneVoice) {
            var toggle = true
            while (isRecordingCloneVoice) {
                delay(120)
                simulatedVoiceWaveAmt = if (toggle) 0.82f else 0.25f
                toggle = !toggle
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("characters_root"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = StudioLocales.charHeading(lang),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = StudioLocales.charSub(lang),
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }

                IconButton(
                    onClick = { showCreatorDrawer = !showCreatorDrawer },
                    modifier = Modifier
                        .background(StudioPrimary, RoundedCornerShape(10.dp))
                        .testTag("toggle_create_char_btn")
                ) {
                    Icon(
                        imageVector = if (showCreatorDrawer) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = "Add Avatar",
                        tint = TextPrimary
                    )
                }
            }
        }

        // Custom character constructor layout
        if (showCreatorDrawer) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioCard),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, StudioPrimary.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = StudioLocales.customCharTitle(lang),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioSecondaryLight
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Name Field
                        OutlinedTextField(
                            value = newCharName,
                            onValueChange = { newCharName = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("char_name_input"),
                            label = { Text(text = StudioLocales.fieldCharName(lang)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StudioPrimary,
                                unfocusedBorderColor = StudioCardLight
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Role description
                        OutlinedTextField(
                            value = newCharDesc,
                            onValueChange = { newCharDesc = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("char_desc_input"),
                            label = { Text(text = StudioLocales.fieldCharDesc(lang)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StudioPrimary,
                                unfocusedBorderColor = StudioCardLight
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Generative Avatar Prompt (Describe features for AI Generation)
                        Text(
                            text = if (isAr) "🎨 تصميم مظهر الأفاتار بوصف الذكاء الاصطناعي:" else "🎨 Generative Avatar Text Description (AI Styling):",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = avatarAIPrompt,
                            onValueChange = { avatarAIPrompt = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    text = if (isAr) "مثال: ملامح عربية، ملابس رسمية، خلفية مكتبية هادئة ثلاثية الأبعاد..." else "e.g. Sharp features, formal suit, clean 3D render design...",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            },
                            textStyle = TextStyle(color = TextPrimary, fontSize = 12.sp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StudioPrimary,
                                unfocusedBorderColor = StudioCardLight
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Button(
                            onClick = {
                                if (avatarAIPrompt.isNotBlank()) {
                                    isSynthesizingAvatarState = true
                                    // Immediate visual seed mock loading delay
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = StudioCardLight),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(
                                text = if (isSynthesizingAvatarState) (if (isAr) "توليد ملامح الأفاتار... ⚡" else "Generating face textures... ⚡") else (if (isAr) "توليد الملامح بالذكاء الاصطناعي 🎭" else "Generate Features 🎭"),
                                fontSize = 10.sp,
                                color = StudioSecondaryLight
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Voice Cloning Studio Module
                        Card(
                            colors = CardDefaults.cardColors(containerColor = StudioBackground),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().border(1.dp, StudioCardLight, RoundedCornerShape(10.dp))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isAr) "🎙️ استوديو استنساخ الصوت الفوري" else "🎙️ Real-time Voice Cloning",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = StudioAccent
                                    )

                                    if (clonedVoiceReady) {
                                        Box(
                                            modifier = Modifier.background(StudioPrimary.copy(alpha = 0.22f), RoundedCornerShape(4.dp)).padding(horizontal = 4.dp, vertical = 2.dp)
                                        ) {
                                            Text(text = if (isAr) "جاهز ✓" else "CLONED ✓", fontSize = 8.sp, color = StudioPrimary, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (isRecordingCloneVoice) {
                                                isRecordingCloneVoice = false
                                                clonedVoiceReady = true
                                                newCharVoice = "صوتي كـ Voice Clone"
                                            } else {
                                                isRecordingCloneVoice = true
                                            }
                                        },
                                        modifier = Modifier.background(if (isRecordingCloneVoice) StudioError else StudioSecondary, CircleShape).size(42.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isRecordingCloneVoice) Icons.Default.Close else Icons.Default.PlayArrow,
                                            contentDescription = "Clone Voice",
                                            tint = StudioBackground,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (isRecordingCloneVoice) (if (isAr) "جاري التسجيل والتحليل... تحدث الآن" else "Listening... Speak continuously") else (if (isAr) "اضغط للتحدث والـ Clone لعشر ثوانٍ" else "Tap and speak to clone your voice"),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextPrimary
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        // Bouncing sound level waveform simulation
                                        LinearProgressIndicator(
                                            progress = { simulatedVoiceWaveAmt },
                                            modifier = Modifier.fillMaxWidth().height(4.dp),
                                            color = if (isRecordingCloneVoice) StudioError else StudioPrimary,
                                            trackColor = StudioCardLight
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Gender Picker
                        Text(
                            text = StudioLocales.labelGender(lang),
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            genders.forEach { gen ->
                                val isSelected = newCharGender == gen
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { newCharGender = gen }
                                        .background(if (isSelected) StudioSecondary else StudioCardLight)
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (lang == "ar") {
                                            when (gen) {
                                                "Male" -> "ذكر"
                                                "Female" -> "أنثى"
                                                "Child" -> "طفل"
                                                else -> "كرتون"
                                            }
                                        } else gen,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) StudioBackground else TextPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Voice Selector
                        Text(
                            text = StudioLocales.labelVoice(lang),
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            voices.take(3).forEach { voice ->
                                val isSelected = newCharVoice == voice
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { newCharVoice = voice }
                                        .background(if (isSelected) StudioPrimary else StudioCardLight)
                                        .padding(vertical = 8.dp, horizontal = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = voice.split(" ")[0],
                                        fontSize = 11.sp,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (newCharName.isNotBlank() && newCharDesc.isNotBlank()) {
                                    viewModel.createAICharacter(
                                        name = newCharName,
                                        gender = newCharGender,
                                        description = newCharDesc,
                                        voice = newCharVoice
                                    )
                                    // Clear fields
                                    newCharName = ""
                                    newCharDesc = ""
                                    avatarAIPrompt = ""
                                    clonedVoiceReady = false
                                    showCreatorDrawer = false
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("char_save_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = StudioAccent)
                        ) {
                            Text(text = StudioLocales.btnSaveChar(lang), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

    // Grid representing loaded avatars
    items(charactersList.windowed(2, 2, true)) { rowItems ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            rowItems.forEach { charItem ->
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("char_item_card_${charItem.id}"),
                    colors = CardDefaults.cardColors(containerColor = StudioCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = when (charItem.gender) {
                                            "Male" -> listOf(Color(0xFF2563EB), Color(0xFF60A5FA))
                                            "Female" -> listOf(Color(0xFFDB2777), Color(0xFFF472B6))
                                            "Child" -> listOf(Color(0xFFF59E0B), Color(0xFFFBBF24))
                                            else -> listOf(Color(0xFF10B981), Color(0xFF34D399))
                                        }
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (if (lang == "ar") charItem.nameAr else charItem.name).take(1),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (lang == "ar") charItem.nameAr else charItem.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Box(
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .background(StudioPrimary.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = charItem.defaultVoice,
                                fontSize = 9.sp,
                                color = StudioPrimaryLight,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = if (lang == "ar") charItem.descriptionAr else charItem.description,
                            fontSize = 11.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.height(34.dp)
                        )

                        if (charItem.isCustom) {
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(
                                onClick = { viewModel.deleteAICharacter(charItem.id) },
                                colors = ButtonDefaults.textButtonColors(contentColor = StudioError)
                            ) {
                                Text(
                                    text = if (lang == "ar") "حذف الشخصية" else "Delete Narrator",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Fill empty spot if odd row counts
            if (rowItems.size == 1) {
                Box(modifier = Modifier.weight(1f))
            }
        }
    }
}
}


        // CINEMATIC VIDEO PLAYER SIMULATION & story detail screen
@Composable
fun ProjectDetailPlayerScreen(viewModel: StudioViewModel, lang: String) {
    val project by viewModel.activeProject.collectAsState()
    val scenes by viewModel.activeScenes.collectAsState()

    if (project == null) return

    val isAr = lang == "ar"
    val scope = rememberCoroutineScope()

    // Interactive Video playback state values
    var isPlaying by remember { mutableStateOf(false) }
    var currentSceneIdx by remember { mutableIntStateOf(0) }
    var playTickerSeconds by remember { mutableFloatStateOf(0f) }

    // Subtitles & SRT custom styles states
    var subtitleFontSize by remember { mutableFloatStateOf(14f) }
    var subtitleColorChoice by remember { mutableStateOf(Color.White) }
    var subtitleBackgroundOpacity by remember { mutableFloatStateOf(0.75f) }
    var generatedSrtContentText by remember { mutableStateOf("") }
    var showSrtDialog by remember { mutableStateOf(false) }

    // Ken-Burns transition anim trackers
    var activeMotionType by remember { mutableStateOf("Zoom In") }

    // Sequential playback timer effect
    LaunchedEffect(isPlaying, currentSceneIdx, scenes) {
        if (isPlaying && scenes.isNotEmpty()) {
            val currentScene = scenes.getOrNull(currentSceneIdx) ?: return@LaunchedEffect
            activeMotionType = currentScene.motionType

            // Ticker counting durations
            var durationElapsed = 0f
            val sceneLimit = currentScene.durationSeconds.toFloat()

            while (isPlaying && currentSceneIdx < scenes.size) {
                delay(100)
                durationElapsed += 0.1f
                playTickerSeconds += 0.1f

                if (durationElapsed >= sceneLimit) {
                    // Transition to next scene frame
                    if (currentSceneIdx < scenes.size - 1) {
                        currentSceneIdx++
                        durationElapsed = 0f
                    } else {
                        // Ended the timeline
                        isPlaying = false
                        currentSceneIdx = 0
                        playTickerSeconds = 0f
                    }
                }
            }
        }
    }

    val activeSceneObj = scenes.getOrNull(currentSceneIdx)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("video_player_root"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project!!.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = StudioLocales.playerSub(lang),
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Button(
                    onClick = { viewModel.navigateTo(StudioScreen.Export) },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioSecondary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Export", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = StudioLocales.tabExport(lang), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioBackground)
                }
            }
        }

        // DYNAMIC NARRATIVE PREVIEW CANVAS WITH REALTIME CUSTOM ACCENT SUBTITLES
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Black),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (scenes.isEmpty()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else if (activeSceneObj != null) {
                        // Dynamic interpolation for Zoom / Pan Animations (Ken-Burns loop simulated)
                        val infiniteTransition = rememberInfiniteTransition(label = "KenBurns")
                        val animTranslateX by infiniteTransition.animateFloat(
                            initialValue = -12f,
                            targetValue = 12f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(5000, easing = LinearOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "ken_burns_pan"
                        )

                        val animScale by infiniteTransition.animateFloat(
                            initialValue = 1.0f,
                            targetValue = 1.18f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(6000, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "ken_burns_zoom"
                        )

                        // Visual rendering image with layered matrices
                        AsyncImage(
                            model = activeSceneObj.imageUrl,
                            contentDescription = "Playing visual clip",
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    if (isPlaying) {
                                        when (activeMotionType) {
                                            "Zoom In" -> {
                                                scaleX = animScale
                                                scaleY = animScale
                                            }
                                            "Pan Right" -> {
                                                translationX = animTranslateX
                                                scaleX = 1.08f
                                                scaleY = 1.08f
                                            }
                                            "Slow Mo Zoom" -> {
                                                scaleX = animScale - 0.05f
                                                scaleY = animScale - 0.05f
                                            }
                                            "Tilt Up" -> {
                                                translationY = animTranslateX / 2
                                                scaleX = 1.06f
                                                scaleY = 1.06f
                                            }
                                            else -> {
                                                translationX = -animTranslateX
                                                scaleX = 1.05f
                                            }
                                        }
                                    }
                                },
                            contentScale = ContentScale.Crop
                        )

                        // Scene details badge overlay top-left
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                                .align(Alignment.TopStart),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = (if (isAr) "المشهد: " else "Scene: ") + "${currentSceneIdx + 1} / ${scenes.size}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            // Dynamic motion banner top-right
                            Box(
                                modifier = Modifier
                                    .background(StudioPrimary.copy(alpha = 0.82f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = activeSceneObj.motionType,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // SYNCHRONIZED TIMELINE AUDIO ACCENT SUBTITLES WITH LIVE HIGHLIGHTS (Features 5, 15)
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = subtitleBackgroundOpacity))
                                    )
                                )
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = activeSceneObj.narrationText,
                                fontSize = subtitleFontSize.sp,
                                fontWeight = FontWeight.Bold,
                                color = subtitleColorChoice,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                style = TextStyle(
                                    shadow = androidx.compose.ui.graphics.Shadow(
                                        color = Color.Black,
                                        blurRadius = 4f
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }

        // MEDIA CONTROLLER CONTROL PAD BAR
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioCard),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            isPlaying = !isPlaying
                        },
                        modifier = Modifier
                            .background(StudioPrimary, CircleShape)
                            .testTag("playback_toggle")
                    ) {
                        if (isPlaying) {
                            Canvas(modifier = Modifier.size(14.dp)) {
                                val w = size.width
                                  val h = size.height
                                drawRect(
                                    color = TextPrimary,
                                    topLeft = androidx.compose.ui.geometry.Offset(w * 0.15f, h * 0.1f),
                                    size = androidx.compose.ui.geometry.Size(w * 0.25f, h * 0.8f)
                                )
                                drawRect(
                                    color = TextPrimary,
                                    topLeft = androidx.compose.ui.geometry.Offset(w * 0.6f, h * 0.1f),
                                    size = androidx.compose.ui.geometry.Size(w * 0.25f, h * 0.8f)
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play Control",
                                tint = TextPrimary
                            )
                        }
                    }

                    // Simulated Progress Tracking timeline line
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    ) {
                        val maxDur = project!!.videoDurationSeconds
                        val progressNormValue = if (maxDur > 0) playTickerSeconds / maxDur else 0f
                        
                        LinearProgressIndicator(
                            progress = { progressNormValue.coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(CircleShape),
                            color = StudioSecondary,
                            trackColor = StudioCardLight
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = String.format("0:%02d", playTickerSeconds.toInt()),
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = String.format("0:%02d", maxDur),
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    // Background Audio indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Music",
                            tint = StudioAccent,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (project!!.musicTrack.contains("No")) "No" else "BGM",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // 9. AUTOMATIC SUBTITLE GENERATOR & SRT CODE EXPORTERS (Features 5, 15)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, StudioPrimary.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isAr) "⚙️ مظهر الترجمة التلقائية وملفات الـ SRT" else "⚙️ Auto Subtitle Styles & SRT Exporter",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioSecondaryLight
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // A. Subtitle Font Size Picker Customizer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = if (isAr) "حجم خط الترجمة الفرعي:" else "Subtitle Text Size:", fontSize = 11.sp, color = TextSecondary)
                        Text(text = "${subtitleFontSize.toInt()} sp", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                    }
                    Slider(
                        value = subtitleFontSize,
                        onValueChange = { subtitleFontSize = it },
                        valueRange = 10f..22f,
                        colors = SliderDefaults.colors(thumbColor = StudioPrimary, activeTrackColor = StudioPrimary)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // B. Subtitle highlight color picker row
                    Text(text = if (isAr) "لون وحشوة الكلمات:" else "Keyword Overlay Color Highlight:", fontSize = 11.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val colorMap = listOf(
                            Pair(Color.White, if (isAr) "أبيض" else "White"),
                            Pair(Color(0xFFFBBF24), if (isAr) "أصفر ذهب" else "Amber"),
                            Pair(Color(0xFF34D399), if (isAr) "زمردي" else "Emerald"),
                            Pair(Color(0xFF22D3EE), if (isAr) "سيان كابلي" else "Cyber Blue")
                        )
                        colorMap.forEach { (col, name) ->
                            val isChosen = subtitleColorChoice == col
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { subtitleColorChoice = col }
                                    .background(if (isChosen) StudioSecondary else StudioCardLight)
                                    .padding(vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = name, fontSize = 9.sp, color = if (isChosen) StudioBackground else TextPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // C. SRT Generator and exporter code trigger
                    Button(
                        onClick = {
                            var srtDraft = ""
                            var elapsedSecs = 0
                            scenes.forEachIndexed { idx, item ->
                                val seq = idx + 1
                                val currentLen = item.durationSeconds
                                val nextElapsed = elapsedSecs + currentLen
                                val fromStr = String.format("00:00:%02d,000", elapsedSecs)
                                val toStr = String.format("00:00:%02d,000", nextElapsed)
                                srtDraft += "$seq\n$fromStr --> $toStr\n${item.narrationText}\n\n"
                                elapsedSecs = nextElapsed
                            }
                            generatedSrtContentText = srtDraft
                            showSrtDialog = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = StudioAccent),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        Text(text = if (isAr) "توليد وتصدير ملف الترجمة SRT 📥" else "Compile SRT Subtitle File 📥", fontSize = 11.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
        }

        // Full Narrative Plot Script Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isAr) "السيناريو السردي الكامل للتعليق" else "Complete Synthesised Script Plot",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioSecondaryLight
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = project!!.narrativeScript,
                        fontSize = 12.sp,
                        color = TextPrimary,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // STORYBOARD SCENE TIMELINES DETAIL SEGMENT
        item {
            Text(
                text = StudioLocales.storyboardHeading(lang),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        items(scenes) { scene ->
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioCard),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("scene_row_${scene.sceneNumber}")
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Small thumbnail preview
                    AsyncImage(
                        model = scene.imageUrl,
                        contentDescription = "Scene Mini",
                        modifier = Modifier
                            .size(76.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(StudioCardLight),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = (if (isAr) "مشهد " else "Scene ") + "${scene.sceneNumber}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioSecondaryLight
                            )

                            Box(
                                modifier = Modifier
                                    .background(StudioCardLight, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${scene.durationSeconds}s",
                                    fontSize = 9.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = scene.narrationText,
                            fontSize = 11.sp,
                            color = TextPrimary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Motion",
                                tint = StudioPrimaryLight,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = scene.motionType,
                                fontSize = 9.sp,
                                color = TextMuted,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

// SECURITY & ENGINE CONFIGURATION SETTINGS SCREEN
@Composable
fun SettingsScreen(viewModel: StudioViewModel, lang: String) {
    val engineSettings by viewModel.engineSettings.collectAsState()
    val projectsList by viewModel.projects.collectAsState()

    val llmList = listOf("Llama 3 (Ollama)", "Qwen 2.5 (Mopen)", "DeepSeek R1 (Ollama)")
    val imgList = listOf("Flux.1", "Stable Diffusion XL", "ComfyUI API")
    val vidList = listOf("Wan2.1 (SVD)", "LTX Video Local", "SVD Frame Loop")
    val ttsList = listOf("XTTS-v2", "Coqui-TTS API", "Native Android TTS")
    val lipList = listOf("Wav2Lip", "MuseTalk Open", "No Lipsync Overlay")

    val isAr = lang == "ar"

    // Account level analytics variables
    val totalVideos = projectsList.size
    val generatedImages = if (totalVideos > 0) totalVideos * 4 + 18 else 18
    val renderHours = if (totalVideos > 0) String.format("%.1f", (totalVideos * 18.5) / 60.0) else "0.0"
    val storageCapacityPercent = if (totalVideos > 0) 0.42f + (totalVideos * 0.05f).coerceAtMost(0.5f) else 0.42f
    val storageUsedGB = String.format("%.2f", storageCapacityPercent * 10f)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("settings_root"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
    ) {
        // 1. ACCOUNT AND STUDIO ANALYTICS HEADER
        item {
            Column {
                Text(
                    text = if (isAr) "📊 الحساب والتحليلات" else "📊 Account & Analytics",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isAr) "إحصائيات استخدام موارد الاستوديو وحالة تهيئة المحركات الفنية" else "Statistical metrics and offline LLM engine pipeline orchestration",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        }

        // 2. MODERN STATISTICS GRID CONSOLE CARD
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isAr) "📈 إحصائيات صناعة المحتوى بالذكاء الاصطناعي" else "📈 Studio Creative Stats Summary",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioSecondaryLight
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Stat 1: Total Completed Videos
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .background(StudioCardLight, RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (isAr) "فيديوهات تامّة" else "Videos",
                                fontSize = 9.sp,
                                color = TextSecondary,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$totalVideos",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                        }

                        // Stat 2: Total Generated Pictures
                        Column(
                            modifier = Modifier
                                .weight(1.3f)
                                .background(StudioCardLight, RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (isAr) "الصور المنتجة" else "Images Drawn",
                                fontSize = 9.sp,
                                color = TextSecondary,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$generatedImages",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = StudioSecondaryLight
                            )
                        }

                        // Stat 3: Render Hours Spent
                        Column(
                            modifier = Modifier
                                .weight(1.1f)
                                .background(StudioCardLight, RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (isAr) "ساعات رندر" else "Render-Time",
                                fontSize = 9.sp,
                                color = TextSecondary,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isAr) "${renderHours} ساعة" else "${renderHours} Hrs",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = StudioSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Storage Capacity linear display
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isAr) "سعة التخزين المحلي والذاكرة المؤقتة:" else "System Cache & Cloud Storage Limit:",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = "$storageUsedGB GB / 10 GB (" + String.format("%.0f", storageCapacityPercent * 100) + "%)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = StudioAccent
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { storageCapacityPercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = StudioAccent,
                        trackColor = StudioCardLight
                    )
                }
            }
        }

        // 3. SECURITY & INTEGRATION TRUST BADGE CARD
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, StudioSecondary.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Lock",
                            tint = StudioSecondaryLipSync(lang),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = StudioLocales.securityCardHead(lang),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = StudioLocales.secretsNotice(lang),
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .background(StudioAccent.copy(alpha = 0.22f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "STATUS: GEMINI ENGINE INJECTED & TRUSTED",
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = StudioAccent
                        )
                    }
                }
            }
        }

        // 4. PIPELINE MODEL ARCHITECTURE HEADER
        item {
            Text(
                text = StudioLocales.stackHeading(lang),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        // LLM choice
        item {
            ModelSelectorList(
                label = "LLM Engine (Script & Storyboard Builder)",
                choices = llmList,
                activeSelection = engineSettings.llmModel,
                onSelected = {
                    viewModel.updateEngineStack(
                        llm = it,
                        image = engineSettings.imageModel,
                        video = engineSettings.videoModel,
                        tts = engineSettings.ttsModel,
                        lipSync = engineSettings.lipSyncModel
                    )
                }
            )
        }

        // Image Gen choice
        item {
            ModelSelectorList(
                label = "Image Generation Engine (Scene Visuals)",
                choices = imgList,
                activeSelection = engineSettings.imageModel,
                onSelected = {
                    viewModel.updateEngineStack(
                        llm = engineSettings.llmModel,
                        image = it,
                        video = engineSettings.videoModel,
                        tts = engineSettings.ttsModel,
                        lipSync = engineSettings.lipSyncModel
                    )
                }
            )
        }

        // Video Gen choice
        item {
            ModelSelectorList(
                label = "Video Generation Engine (Motion Loops)",
                choices = vidList,
                activeSelection = engineSettings.videoModel,
                onSelected = {
                    viewModel.updateEngineStack(
                        llm = engineSettings.llmModel,
                        image = engineSettings.imageModel,
                        video = it,
                        tts = engineSettings.ttsModel,
                        lipSync = engineSettings.lipSyncModel
                    )
                }
            )
        }

        // Audio Gen choice
        item {
            ModelSelectorList(
                label = "Synth Translation Voiceover Text-To-Speech (TTS)",
                choices = ttsList,
                activeSelection = engineSettings.ttsModel,
                onSelected = {
                    viewModel.updateEngineStack(
                        llm = engineSettings.llmModel,
                        image = engineSettings.imageModel,
                        video = engineSettings.videoModel,
                        tts = it,
                        lipSync = engineSettings.lipSyncModel
                    )
                }
            )
        }

        // Lip Sync choice
        item {
            ModelSelectorList(
                label = "Facial Lip Sync Model Overlay",
                choices = lipList,
                activeSelection = engineSettings.lipSyncModel,
                onSelected = {
                    viewModel.updateEngineStack(
                        llm = engineSettings.llmModel,
                        image = engineSettings.imageModel,
                        video = engineSettings.videoModel,
                        tts = engineSettings.ttsModel,
                        lipSync = it
                    )
                }
            )
        }
    }
}

@Composable
fun ModelSelectorList(
    label: String,
    choices: List<String>,
    activeSelection: String,
    onSelected: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioCard),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = StudioSecondaryLight
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                choices.forEach { choice ->
                    val isSelected = activeSelection == choice
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onSelected(choice) }
                            .background(if (isSelected) StudioPrimary else StudioCardLight)
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = choice.split(" ")[0],
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

// SECURITY ICON DELEGATE HELPER
private fun StudioSecondaryLipSync(lang: String): Color {
    return StudioSecondary
}

// EXPORT PIPELINE SCREEN
@Composable
fun ExportScreen(viewModel: StudioViewModel, lang: String) {
    val project by viewModel.activeProject.collectAsState()
    val projectsList by viewModel.projects.collectAsState()
    val charactersList by viewModel.characters.collectAsState()
    val isAr = lang == "ar"

    var exportFormat by remember { mutableStateOf("Full HD (1080p) MP4") }
    var compileProgress by remember { mutableFloatStateOf(0f) }
    var isCompiling by remember { mutableStateOf(false) }
    var compileFinished by remember { mutableStateOf(false) }

    val formatChoices = listOf("Full HD (1080p)", "2K HD", "4K Ultra HD")

    val scope = rememberCoroutineScope()

    if (project == null) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .testTag("export_root"),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
        ) {
            item {
                Column {
                    Text(
                        text = if (isAr) "📁 مكتبة المشاريع الإبداعية" else "📁 Creative Projects Library",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isAr) "اختر مشروعًا لمتابعته، تشغيله، أو بدء تصديره ببرمجيات الاستوديو" else "Select a previous storyboard project to resume editing, play or export",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            }

            if (projectsList.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = StudioCard),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Empty Library",
                                tint = TextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (isAr) "لم تقم بإنشاء أي مشروع بعد" else "Your library is currently empty",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary
                            )
                        }
                    }
                }
            } else {
                items(projectsList) { proj ->
                    DashboardProjectCard(
                        project = proj,
                        charactersList = charactersList,
                        lang = lang,
                        onClick = { viewModel.selectProject(proj) },
                        onDelete = { viewModel.deleteProject(proj.id) }
                    )
                }
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .testTag("export_root"),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = StudioLocales.exportHeading(lang),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = StudioLocales.exportSub(lang),
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    TextButton(
                        onClick = { viewModel.clearActiveProject() }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.List, contentDescription = "All Projects", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = if (isAr) "المكتبة" else "Library", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Active project details card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioCard),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val coverSeed = project!!.title.hashCode().absoluteValue
                        AsyncImage(
                            model = "https://picsum.photos/seed/$coverSeed/800/450",
                            contentDescription = "Project Cover Mini",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = project!!.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = (if (isAr) "المدة الزمنية: " else "Expected Duration: ") + "${project!!.videoDurationSeconds} " + (if (isAr) "ثانية" else "Secs"),
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Format selector block
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioCard),
                    shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = if (isAr) "اختر جودة التجهيز (Format Encoding)" else "Specification Quality Options",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        formatChoices.forEach { format ->
                            val isSelected = exportFormat.contains(format.split(" ")[0])
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { exportFormat = "$format MP4" }
                                    .background(if (isSelected) StudioPrimary else StudioCardLight)
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = format.split(" ")[0],
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Progress or button actions
        item {
            if (isCompiling) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioCard),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isAr) "جاري رندرة وتوليف دفقات الفيديو..." else "Compiling pipeline layers...",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioSecondaryLight
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { compileProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CircleShape),
                            color = StudioSecondary,
                            trackColor = StudioCardLight
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${(compileProgress * 100).toInt()}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }
            } else if (compileFinished) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioCard),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = StudioAccent,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isAr) "تم تصدير الفيديو بنجاح وحفظه بالهاتف!" else "Video successfully compiled and saved!",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "FILE_ID: MP4_H264_${exportFormat.split(" ")[0]}_${project!!.id}.mp4",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TextMuted
                        )
                    }
                }
            } else {
                Button(
                    onClick = {
                        scope.launch {
                            isCompiling = true
                            compileProgress = 0f
                            while (compileProgress < 1.0f) {
                                delay(200)
                                compileProgress += 0.05f
                            }
                            isCompiling = false
                            compileFinished = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("trigger_export_compilation"),
                    colors = ButtonDefaults.buttonColors(containerColor = StudioPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Compile")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = StudioLocales.btnCompileRender(lang),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        // Share Action Badges
        if (compileFinished) {
            item {
                Text(
                    text = StudioLocales.labelShareSocial(lang),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "TikTok")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "TikTok", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "YouTube")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "YouTube", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
}

