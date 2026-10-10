package freelancerhelper.aab.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class TipCategory(val displayName: String) {
    ALL("All Tips"),
    PROPOSALS("Proposals & First Job 🎯"),
    PROFILE_HEADLINES("Profile & Headlines ✍️"),
    TECH_DEV("Tech & Development 💻"),
    WRITING_TRANSLATION("Writing & Translation 📝"),
    SALES_MARKETING("Sales & Marketing 📈"),
    DESIGN_CREATIVE("Design & Creative 🎨"),
    ADMIN_SUPPORT("Admin & Support 💼"),
    PRICING_ALGORITHM("Pricing & Algorithm 💰"),
    GLOBAL_PLAYBOOK("Global Playbook 🌍")
}

data class TipItem(
    val id: String,
    val title: String,
    val category: TipCategory,
    val summary: String,
    val detail: String,
    val dos: List<String>,
    val donts: List<String>,
    val formula: String? = null,
    val profession: String? = null,
    val isBookmarked: Boolean = false,
    val isRead: Boolean = false
)

data class TipsUiState(
    val searchQuery: String = "",
    val selectedCategory: TipCategory = TipCategory.ALL,
    val expandedTipId: String? = "tip_proposal_hook_loom",
    val tips: List<TipItem> = DEFAULT_TIPS
) {
    val filteredTips: List<TipItem>
        get() = tips.filter { tip ->
            val matchesCategory = selectedCategory == TipCategory.ALL || tip.category == selectedCategory
            val matchesQuery = searchQuery.isBlank() ||
                    tip.title.contains(searchQuery, ignoreCase = true) ||
                    tip.summary.contains(searchQuery, ignoreCase = true) ||
                    tip.detail.contains(searchQuery, ignoreCase = true) ||
                    tip.category.displayName.contains(searchQuery, ignoreCase = true) ||
                    (tip.profession?.contains(searchQuery, ignoreCase = true) == true) ||
                    (tip.formula?.contains(searchQuery, ignoreCase = true) == true) ||
                    tip.dos.any { it.contains(searchQuery, ignoreCase = true) } ||
                    tip.donts.any { it.contains(searchQuery, ignoreCase = true) }
            matchesCategory && matchesQuery
        }

    val readCount: Int
        get() = tips.count { it.isRead }

    val bookmarkedCount: Int
        get() = tips.count { it.isBookmarked }
}

class TipsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TipsUiState())
    val uiState: StateFlow<TipsUiState> = _uiState.asStateFlow()

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun selectCategory(category: TipCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun toggleExpand(tipId: String) {
        _uiState.update { current ->
            val newExpanded = if (current.expandedTipId == tipId) null else tipId
            current.copy(expandedTipId = newExpanded)
        }
    }

    fun toggleBookmark(tipId: String) {
        _uiState.update { current ->
            val updated = current.tips.map { tip ->
                if (tip.id == tipId) tip.copy(isBookmarked = !tip.isBookmarked) else tip
            }
            current.copy(tips = updated)
        }
    }

    fun toggleRead(tipId: String) {
        _uiState.update { current ->
            val updated = current.tips.map { tip ->
                if (tip.id == tipId) tip.copy(isRead = !tip.isRead) else tip
            }
            current.copy(tips = updated)
        }
    }
}

val DEFAULT_TIPS = listOf(
    // 1. Proposals & First Job 🎯
    TipItem(
        id = "tip_proposal_hook_loom",
        title = "3-Line Hook & Custom Video Pitch Strategy",
        category = TipCategory.PROPOSALS,
        profession = "Tech/Dev, Design, Marketing, Writing, VA",
        summary = "Hook clients in the first 3 lines of your proposal preview and boost responses with a 60-second video.",
        detail = "Freelancer previews only the top 2-3 lines of a proposal before a client clicks 'Read More'. Never waste line 1 on formal intros like 'Dear Hiring Manager'. Open with a direct solution statement, a relevant proof point, or a custom Loom video link.",
        dos = listOf(
            "Address the client's core problem immediately in sentence 1",
            "Include a 60-90 second Loom video breaking down a quick audit or past project result",
            "End with a low-friction technical or scope question"
        ),
        donts = listOf(
            "Don't start with 'Hi, I am a passionate developer eagerly applying for this job'",
            "Don't paste massive walls of generic resume text without bullet points"
        ),
        formula = "Line 1: Hi [Name], I saw your post regarding [Specific Job Challenge] and built a quick solution outline.\nLine 2: Here is a 60s Loom video breaking down how I'd approach this: [Loom URL]\nLine 3: Are you available for a brief 10-minute chat this week to align on specs?"
    ),
    TipItem(
        id = "tip_proposal_problem_first",
        title = "Problem-First Proposal Framework",
        category = TipCategory.PROPOSALS,
        profession = "Writing & Translation, Marketing, VA",
        summary = "Structure proposals around client business goals rather than your personal resume.",
        detail = "Clients hire outcomes, not credentials. For copywriting, translation, or admin support, immediately outline how your workflow eliminates their bottleneck, saving time or boosting sales.",
        dos = listOf(
            "Reference specific job post details (target audience, tools, milestones)",
            "Attach 1 hyper-relevant case study instead of multiple generic files",
            "Ask 1-2 clarifying questions that demonstrate deep domain expertise"
        ),
        donts = listOf(
            "Don't send canned template proposals without client customization",
            "Don't compete on lowest price—compete on clarity, speed, and process"
        ),
        formula = "1. Specific greeting & problem acknowledgment\n2. Relevant proof / case study link\n3. Step-by-step action plan\n4. Call to action: 'Free 15-min alignment call this week?'"
    ),
    TipItem(
        id = "tip_proposal_response_rates",
        title = "Optimizing Proposal Response Rates & Fast Follow-Ups",
        category = TipCategory.PROPOSALS,
        profession = "All Professions",
        summary = "Maximize proposal conversion rates by tracking view-to-interview metrics and sending 24-hour follow-ups.",
        detail = "Top-performing freelancers achieve 20%+ proposal response rates by submitting within the first 3 hours of job posting and following up on viewed proposals with additional insights.",
        dos = listOf(
            "Apply to newly posted jobs (<3 hours old) with fewer than 10-15 proposals",
            "Track proposal view notifications and send a short follow-up note within 24 hours",
            "Tailor connects bid amounts based on job fit and client hiring history"
        ),
        donts = listOf(
            "Don't waste connects bidding on unverified payment clients with 0% hire rate",
            "Don't send aggressive double-messages if a client hasn't viewed your proposal yet"
        ),
        formula = "Follow-Up Template: 'Hi [Name], I noticed you checked out my proposal! I just updated a quick prototype related to your project requirement—here is the link if you'd like to see it in action: [Link].'"
    ),

    // 2. Profile & Headlines ✍️
    TipItem(
        id = "tip_headline_formula",
        title = "The 2026 Value-Driven Headline Formula",
        category = TipCategory.PROFILE_HEADLINES,
        profession = "Tech/Dev & All Professions",
        summary = "Your headline is your billboard on Freelancer search. Rank higher by pairing role, specialization, and impact.",
        detail = "High-earning freelancers across Dev, Design, Marketing, Writing, and VA roles avoid vague titles. Combine your core skill stack with the quantifiable outcome you deliver for clients.",
        dos = listOf(
            "Include core search keywords (e.g. Kotlin, Jetpack Compose, UX Audit, B2B Copywriting)",
            "Highlight tangible client impact (e.g. 'Optimizing App Conversion & Performance')",
            "Keep headline under 70 characters so it scans easily in proposal lists"
        ),
        donts = listOf(
            "Don't write generic titles like 'Hardworking Programmer' or 'Virtual Assistant'",
            "Don't list 10 unrelated technologies in one line",
            "Don't write in ALL CAPS or use excessive emojis"
        ),
        formula = "[Role/Niche] | [Core Tools/Stack] | [Key Outcome]\nExample (Dev): Senior Android Engineer | Kotlin & Compose | Scalable Apps & Performance\nExample (Design): Figma UX/UI Designer | SaaS Web & Mobile | 30% Higher Conversion"
    ),
    TipItem(
        id = "tip_profile_completeness_seo",
        title = "100% Profile Completeness & SEO Bio Formulas",
        category = TipCategory.PROFILE_HEADLINES,
        profession = "All Professions",
        summary = "Boost search visibility and client trust with structured bio sections, specialized profiles, and video intros.",
        detail = "Freelancer's search engine indexes your bio summary, specialized profile titles, skill tags, and portfolio descriptions. Achieve 100% profile completeness and insert client search terms naturally in the first 250 characters of your bio.",
        dos = listOf(
            "Create 2 Specialized Profiles tailored to distinct high-demand sub-niches",
            "Include a 30-60 second video intro introducing your workflow and client results",
            "Structure bio with clean headers: Core Skills, Past Successes, and How We Work Together"
        ),
        donts = listOf(
            "Don't write long narrative paragraphs about your life story or university courses",
            "Don't leave portfolio items without detailed outcome descriptions and visual thumbnails"
        ),
        formula = "Bio Structure:\n• Line 1-2: Core value proposition & top specialization\n• Section 1: Proven Results & Case Metrics\n• Section 2: Core Stack & Methodology\n• Section 3: Call to Action ('Send an invite to discuss your project!')"
    ),

    // 3. Tech & Development 💻
    TipItem(
        id = "tip_tech_dev_stack",
        title = "Full-Stack, Mobile & AI Integration Positioning",
        category = TipCategory.TECH_DEV,
        profession = "Full-Stack, Mobile Dev, AI Engineers",
        summary = "Position your tech profile around high-demand stacks like Jetpack Compose, React, Python, and AI APIs.",
        detail = "Clients hire developers who bridge modern frontend frameworks (Compose/React) with backend APIs, Python data pipelines, and LLM integrations (OpenAI/Anthropic/Gemini APIs). Showcase live code repositories and architectural clarity.",
        dos = listOf(
            "Highlight experience with Kotlin/Jetpack Compose, Python, React, and REST/GraphQL APIs",
            "Demonstrate AI API integrations (LLM prompt engineering, RAG pipelines, OpenAI/Gemini SDKs)",
            "Include architecture patterns (MVVM, Clean Architecture, CI/CD pipelines) in project descriptions"
        ),
        donts = listOf(
            "Don't list outdated legacy frameworks without mentioning modern equivalents",
            "Don't submit code samples without clear documentation or README setup guides"
        ),
        formula = "Tech Specialist Pitch: 'Senior [Mobile/Full-Stack] Engineer specializing in [Kotlin Compose / React / Python] and LLM AI API integrations. I build scalable, high-performance applications with <0.1% crash rates.'"
    ),

    // 4. Writing & Translation 📝
    TipItem(
        id = "tip_writing_translation_mastery",
        title = "High-Converting Copywriting & Technical Translation",
        category = TipCategory.WRITING_TRANSLATION,
        profession = "Copywriters, Content Writers, Translators",
        summary = "Master B2B SaaS copywriting, SEO article structuring, and domain-specific technical translation.",
        detail = "Clients in tech, finance, and e-commerce pay premium rates for content that converts readers into buyers or translates complex technical documents accurately across languages.",
        dos = listOf(
            "Structure articles with SEO-driven H2/H3 headers, meta descriptions, and clear CTAs",
            "Showcase conversion metrics from past campaigns (e.g. +40% email open rates)",
            "Offer dual-language localization with cultural and technical accuracy"
        ),
        donts = listOf(
            "Don't deliver raw unedited AI text without personal domain expertise and human editing",
            "Don't write generic blog posts without keyword research or audience targeting"
        ),
        formula = "Copywriting Pitch: 'I help SaaS & E-commerce brands scale organic traffic and conversions with SEO-optimized articles, sales landing pages, and technical translations.'"
    ),

    // 5. Sales & Marketing 📈
    TipItem(
        id = "tip_sales_marketing_growth",
        title = "B2B Lead Generation & Social Media Growth",
        category = TipCategory.SALES_MARKETING,
        profession = "Sales & Marketing Specialists",
        summary = "Drive measurable pipeline growth through targeted lead gen, cold email outreach, and short-form video marketing.",
        detail = "High-paying marketing clients want qualified booked calls and revenue growth, not vanity metrics. Position your services around cold outreach automation, LinkedIn/Meta ad scaling, and short-form video engagement (Reels/TikTok).",
        dos = listOf(
            "Define clear lead qualification criteria and email deliverability metrics",
            "Set up multi-channel cold outreach sequences (LinkedIn + Email + CRM tracking)",
            "Share verifiable ROAS (Return on Ad Spend) or booked call statistics"
        ),
        donts = listOf(
            "Don't offer unverified spam prospect lists without validation",
            "Don't guarantee revenue without audit of client sales funnel and product-market fit"
        ),
        formula = "Lead Gen Pitch: 'I build automated B2B lead generation funnels that book 15-30 qualified sales calls per month for SaaS & agency founders.'"
    ),

    // 6. Design & Creative 🎨
    TipItem(
        id = "tip_design_creative_execution",
        title = "UI/UX Figma Design Systems & Video Editing",
        category = TipCategory.DESIGN_CREATIVE,
        profession = "UI/UX Designers, Video Editors, Brand Designers",
        summary = "Win high-budget creative projects with interactive Figma prototypes, brand systems, and short-form video edits.",
        detail = "Clients hire designers who streamline developer handoffs with complete Figma design systems and auto-layout components. Video editors win contracts by delivering fast-paced, high-retention video edits for social channels.",
        dos = listOf(
            "Provide clickable Figma prototypes showing micro-interactions and responsive layouts",
            "Organize design tokens (colors, typography, spacing) for seamless developer integration",
            "Include short video reel highlights showcasing editing speed, transitions, and captions"
        ),
        donts = listOf(
            "Don't submit static unorganized image files without component design tokens",
            "Don't ignore mobile responsiveness in UI layout prototypes"
        ),
        formula = "Design Pitch: 'Senior UI/UX Designer & Figma Specialist. I craft high-converting SaaS interfaces and design systems optimized for developer handoff and seamless mobile responsiveness.'"
    ),

    // 7. Admin & Support 💼
    TipItem(
        id = "tip_admin_support_systems",
        title = "Executive VA, Automated Workflows & Project Management",
        category = TipCategory.ADMIN_SUPPORT,
        profession = "Virtual Assistants, Project Managers, Support Leads",
        summary = "Scale operations for busy founders using AI workflow automation, inbox zero systems, and agile project management.",
        detail = "Modern virtual assistants and project managers go beyond basic administrative tasks by setting up Notion/Asana workspaces, automating repetitive workflows with Zapier/Make, and handling customer support channels.",
        dos = listOf(
            "Implement automated CRM and inbox management frameworks",
            "Set up clear project tracking boards in Asana, Jira, or Notion",
            "Highlight experience with customer support tools (Zendesk, Intercom, Freshdesk)"
        ),
        donts = listOf(
            "Don't list basic data entry without highlighting efficiency or automation tools",
            "Don't leave client communication channels unmonitored during agreed shift hours"
        ),
        formula = "Executive VA Pitch: 'Executive Assistant & Operations Specialist. I help founders save 15+ hours/week by managing inbox/calendar systems, customer support, and Zapier automated workflows.'"
    ),

    // 8. Pricing & Algorithm 💰
    TipItem(
        id = "tip_value_pricing_fixed_vs_hourly",
        title = "Fixed-Price Value Framing vs Hourly Billing",
        category = TipCategory.PRICING_ALGORITHM,
        profession = "Tech/Dev, Design, Sales & Marketing",
        summary = "Transition from selling hours to pricing outcomes to double your effective hourly rate.",
        detail = "As you gain speed and expertise, hourly billing penalizes efficiency. Shift towards fixed-price milestones tied to deliverable value (e.g. 'Landing Page UX Redesign - $2,500' instead of '25 hrs @ $100/hr').",
        dos = listOf(
            "Define clear scope, deliverables, and revision limits for fixed-price contracts",
            "Break large projects into 2-4 milestone payments (e.g. 30% deposit, 50% beta, 20% launch)",
            "Increase rates by 15-20% after every 3 successful client contracts"
        ),
        donts = listOf(
            "Don't start fixed-price work without funded milestones on Freelancer escrow",
            "Don't allow scope creep without creating a paid milestone addendum"
        ),
        formula = "Value Milestone Breakdown:\nMilestone 1: Discovery & System Architecture (30%)\nMilestone 2: Production Build & Testing (50%)\nMilestone 3: Deployment & Client Handoff (20%)"
    ),
    TipItem(
        id = "tip_jss_private_feedback",
        title = "Private Feedback Secrets & JSS Shielding",
        category = TipCategory.PRICING_ALGORITHM,
        profession = "All Professions",
        summary = "Master the hidden impact of private client feedback on your Freelancer Job Success Score (JSS).",
        detail = "Your Freelancer Job Success Score is heavily determined by PRIVATE client feedback ('How likely are you to recommend this freelancer? 1-10'). Public 5-star ratings don't guarantee a high JSS if private feedback is lukewarm.",
        dos = listOf(
            "Set explicit expectations before accepting contract offers",
            "Ask clients at project close: 'Was there anything we could have done better?'",
            "Use the Top Rated Perk to remove 1 negative score per 3 months/10 contracts"
        ),
        donts = listOf(
            "Don't leave inactive contracts open for months without progress",
            "Don't argue with clients over final milestone approvals"
        ),
        formula = "Contract Closing Script: 'Thank you for the great collaboration! I've submitted the final deliverable. Once approved, I'd appreciate your honest feedback. If you enjoyed working together, a 10/10 recommendation helps my business tremendously!'"
    ),
    TipItem(
        id = "tip_monthly_retainers",
        title = "Securing Monthly Retainers & Recurring Revenue",
        category = TipCategory.PRICING_ALGORITHM,
        profession = "Virtual Assistance/Admin, Marketing, Writing",
        summary = "Turn one-off Freelancer projects into predictable monthly recurring revenue through strategic negotiation.",
        detail = "Clients hate rehiring. After completing an initial project, offer a recurring monthly retainer package to maintain momentum and protect long-term earnings.",
        dos = listOf(
            "Offer retainer options (e.g. guaranteed 15-20 hrs/week at a locked rate)",
            "Pitch ongoing system maintenance, content updates, or inbox management",
            "Use Freelancer's Weekly Allowance feature for smooth billing"
        ),
        donts = listOf(
            "Don't pitch retainers before delivering exceptional initial project results",
            "Don't offer unlimited availability without clear weekly cap limits"
        ),
        formula = "Retainer Upsell Pitch: 'Now that we've completed [Initial Project], I can offer a monthly retainer of [X] hours/week to handle [Ongoing Need] so you don't have to worry about upkeep. Shall I set up a recurring contract?'"
    ),

    // 9. Global Playbook 🌍
    TipItem(
        id = "tip_global_eastern_europe",
        title = "Eastern Europe: Tech & Engineering Powerhouse Strategy",
        category = TipCategory.GLOBAL_PLAYBOOK,
        profession = "Eastern Europe (Tech & Engineering Focus)",
        summary = "Capitalize on strong technical & CS backgrounds by framing technical depth into business ROI and leveraging EST/CET overlap.",
        detail = "Eastern European engineers possess exceptional technical education and problem-solving depth, but often under-sell their business impact or accept low hourly rates. Win mid-to-enterprise US/EU tech clients by framing technical architecture in terms of quantifiable business ROI, code maintainability, and real-time time zone overlap.",
        dos = listOf(
            "Quantify business ROI (e.g., reduced API latency by 45%, cut crash rates to <0.1%)",
            "Optimize work overlap during US EST and Europe CET business hours for real-time sync",
            "Target mid-to-enterprise US/EU tech clients looking for senior engineering depth",
            "Frame technical depth and architecture quality over cheap hourly rates"
        ),
        donts = listOf(
            "Don't sell yourself as cheap hourly labor—position as a high-value software partner",
            "Don't list raw tech specs without explaining how they benefit client revenue or scale",
            "Don't let technical understatement hide your architectural leadership"
        ),
        formula = "Profile Headline: Senior Android & Systems Architect | Kotlin & Jetpack Compose | Enterprise ROI & US EST Overlap\nProposal Hook: 'Hi [Name], I noticed your app faces performance bottlenecks. Leveraging my CS & architecture background, I can optimize render performance while syncing directly during your EST/CET business hours.'"
    ),
    TipItem(
        id = "tip_global_south_asia",
        title = "South & SE Asia: Niche Positioning & Loom Video Strategy",
        category = TipCategory.GLOBAL_PLAYBOOK,
        profession = "South & SE Asia (India, Philippines, Pakistan, Bangladesh)",
        summary = "Escape the low-price trap by specializing in high-converting sub-niches, using Loom video pitches, and enforcing milestone boundaries.",
        detail = "Freelancers in South and Southeast Asia have massive skill variety, high flexibility, and English fluency, but frequently encounter low-price competition in generalist categories. Elevate your positioning by specializing in high-ROI sub-niches (e.g., Shopify CRO over general VA), building trust with short Loom video pitches, and setting firm milestone scope boundaries.",
        dos = listOf(
            "Specialize in high-converting sub-niches (e.g., Shopify CRO or B2B Lead Gen over general VA)",
            "Attach a 60-second Loom video introduction to build instant trust and showcase fluency",
            "Establish clear milestone scope boundaries to protect against scope creep",
            "Highlight speed, responsiveness, and end-to-end task execution"
        ),
        donts = listOf(
            "Don't participate in race-to-the-bottom hourly pricing wars against generalists",
            "Don't send canned template proposals without personalized video or solution hooks",
            "Don't accept unfunded milestone requests or ambiguous scope lists"
        ),
        formula = "Loom Pitch Formula: 'Hi [Name]! I created a quick 60-second Loom video [URL] walking through my exact strategy to improve conversion for your Shopify store. Let's discuss funded Milestone 1 when you're ready!'"
    ),
    TipItem(
        id = "tip_global_africa",
        title = "Africa: Infrastructure Proof & Native English Advantage",
        category = TipCategory.GLOBAL_PLAYBOOK,
        profession = "Africa (Nigeria, Kenya, South Africa, Egypt, Ghana)",
        summary = "Highlight infrastructure reliability (backup power/fiber), set up verified Freelancer payment methods early, and target US/UK/EU markets.",
        detail = "Africa's booming tech and creative talent pool offers fluent English, strong tech/creative capabilities, and cost efficiency. Address common international client concerns proactively by highlighting guaranteed infrastructure reliability (backup power/high-speed fiber), setting up verified Freelancer payment methods early (Payoneer/Wise), and targeting US/UK/EU clients looking for native English content, VA, or software engineering.",
        dos = listOf(
            "Highlight infrastructure reliability upfront (e.g., backup power / high-speed fiber connection)",
            "Set up verified Freelancer payout methods early (Payoneer / Wise / direct bank integration)",
            "Target US, UK, and EU clients seeking high-quality native English content, VA, or software engineering",
            "Showcase portfolio proof and client testimonials right at the top of proposals"
        ),
        donts = listOf(
            "Don't leave payment method verification until after winning your first contract",
            "Don't leave client doubts about power or internet connectivity unaddressed in your intro",
            "Don't underprice high-quality native English writing or specialized engineering talent"
        ),
        formula = "Infrastructure Guarantee Tagline: 'Operating with dedicated high-speed fiber internet and dual solar/generator power backup for uninterrupted delivery across US/UK/EU client timezones.'"
    ),
    TipItem(
        id = "tip_global_latam",
        title = "Latin America (LATAM): US Nearshore & Timezone Strategy",
        category = TipCategory.GLOBAL_PLAYBOOK,
        profession = "LATAM (Mexico, Brazil, Argentina, Colombia)",
        summary = "Position as a premium 'US Nearshore' partner with direct time zone alignment (EST/CST/PST) and real-time Slack/Zoom sync.",
        detail = "LATAM freelancers share nearshore US time zone alignment (EST/CST/PST) and cultural fluency with North American startups. Position your profile around nearshore real-time collaboration, daily agile standups, and zero-delay communication.",
        dos = listOf(
            "Feature 'US Nearshore / Same-Timezone' directly in your profile headline and proposals",
            "Pitch real-time Slack, Teams, and Zoom collaboration during North American business hours",
            "Target fast-growing US and Canadian startups that demand synchronous team communication",
            "Emphasize cultural fluency, quick feedback loops, and agile sprint participation"
        ),
        donts = listOf(
            "Don't market yourself as an async-only worker when you share exact same timezones",
            "Don't miss the chance to join live client team standups during US business hours",
            "Don't charge low offshore rates when delivering high-value nearshore synchronous work"
        ),
        formula = "Nearshore Headline & Hook: Headline: 'Senior Full-Stack Engineer | US Nearshore (EST/PST) | Real-time Agile Sync'\nHook: 'Hi [Name], being in the same timezone (EST), I can join your daily Slack standups and push code during your core team hours.'"
    ),
    TipItem(
        id = "tip_global_mena",
        title = "MENA: Trilingual Localization & Regional Expansion Playbook",
        category = TipCategory.GLOBAL_PLAYBOOK,
        profession = "Middle East & North Africa (MENA)",
        summary = "Leverage dual/triple language capability (Arabic, French, English) to lead regional market expansion and RTL design.",
        detail = "Freelancers in the Middle East & North Africa (MENA) region hold a distinct advantage in dual/triple language capability (Arabic, French, English) and regional market nuances. Capitalize on high-value contracts by specializing in MENA market expansion, Arabic/English localization, Right-to-Left (RTL) app design, and regional digital marketing.",
        dos = listOf(
            "Specialize in MENA market expansion, Arabic/English localization, and regional digital marketing",
            "Highlight expertise in Right-to-Left (RTL) UI/UX design and trilingual cultural localization",
            "Target Western SaaS, E-commerce, and gaming brands expanding into Gulf/GCC and North African markets",
            "Position trilingual capability (Arabic, French, English) as an asset for multi-market global campaigns"
        ),
        donts = listOf(
            "Don't offer generic translation—position as a high-value market entry and localization strategist",
            "Don't ignore the booming e-commerce and fintech expansion across UAE, KSA, and North Africa",
            "Don't forget to showcase RTL design samples in your Freelancer portfolio"
        ),
        formula = "MENA Expansion Pitch: 'Hi [Name], launching your product in the GCC & MENA region? I provide end-to-end Arabic/English localization, RTL UI design, and region-specific digital marketing to capture high-value Middle Eastern buyers.'"
    )
)

