import { Routes, Route } from 'react-router-dom';
import { ToastProvider } from './context/ToastContext';
import { lazy, Suspense } from 'react';
import StaggeredMenu from './components/StaggeredMenu';
import Footer from './components/Footer';
import { useAuthCheck } from './hooks/authCheck';
import { logout } from './api/authApi';
import ScrollToTop from './components/ScrollToTop';

const HomePage = lazy(() => import('./pages/HomePage'));
const AboutPage = lazy(() => import('./pages/AboutPage'));
const LoginPage = lazy(() => import('./pages/LoginPage'));
const RegisterPage = lazy(() => import('./pages/RegisterPage'));
const ChatAiPage = lazy(() => import('./pages/ChatAiPage'));
const PreAdoptionPage = lazy(() => import('./pages/PreAdoptionPage'));
const FirstTwoMonthsPage = lazy(() => import('./pages/FirstTwoMonthsPage'));
const SocializationPage = lazy(() => import('./pages/SocializationPage'));
const TrainingGuidePage = lazy(() => import('./pages/TrainingGuidePage'));
const PetiquettePage = lazy(() => import('./pages/PetiquettePage'));
const ChecklistPage = lazy(() => import('./pages/ChecklistPage'));
const ContactPage = lazy(() => import('./pages/ContactPage'));
const PrivacyPolicyPage = lazy(() => import('./pages/PrivacyPolicyPage'));
const TermsOfServicePage = lazy(() => import('./pages/TermsOfServicePage'));

function App() {
    const { loggedIn, checkAuth } = useAuthCheck();
    const handleLogout = async () => {
        try {
            await logout();
            await checkAuth();
        } catch (e) {
            alert('Logout failed: ' + e.message);
        }
    };

    const menuItems = [
        { label: '홈', ariaLabel: 'Go to home page', link: '/' },
        { label: '체크리스트', ariaLabel: 'Go to checklist page', link: '/checklist' },
        { label: '입양 전', ariaLabel: 'Learn about pre-adoption', link: '/pre-adoption' },
        { label: '초기 2개월', ariaLabel: 'Learn about the first 2 months', link: '/first-2-months' },
        { label: '사회화', ariaLabel: 'Learn about 2-4 months socialization', link: '/2-4-months' },
        { label: '훈련 가이드', ariaLabel: 'Learn training guide', link: '/training-guide' },
        { label: '펫티켓', ariaLabel: 'Learn about petiquette', link: '/petiquette' },
        { label: '소개', ariaLabel: 'Learn about us', link: '/about' },
        { label: '문의', ariaLabel: 'Contact us', link: '/contact' },
        loggedIn
            ? { label: '로그아웃', ariaLabel: 'Logout from your account', onClick: handleLogout }
            : { label: '로그인', ariaLabel: 'Login with your account', link: '/login' },
    ];

    return (
        <ToastProvider>
            <ScrollToTop />

            <StaggeredMenu
                    position="right"
                    items={menuItems}
                    displaySocials={false}
                    displayItemNumbering={true}
                    menuButtonColor="#fff"
                    openMenuButtonColor="#000"
                    changeMenuColorOnOpen={true}
                    colors={['rgb(232, 62, 57)', 'rgb(242, 143, 63)']}
                    accentColor="rgb(242, 143, 63)"
                />

            <Suspense fallback={<div>Loading...</div>}>
                <Routes>
                    <Route path="/" element={<HomePage />} />
                    <Route path="/pre-adoption" element={<PreAdoptionPage />} />
                    <Route path="/first-2-months" element={<FirstTwoMonthsPage />} />
                    <Route path="/2-4-months" element={<SocializationPage />} />
                    <Route path="/training-guide" element={<TrainingGuidePage />} />
                    <Route path="/petiquette" element={<PetiquettePage />} />
                    <Route path="/checklist" element={<ChecklistPage />} />
                    <Route path="/chat-ai" element={<ChatAiPage />} />
                    <Route path="/about" element={<AboutPage />} />
                    <Route path="/contact" element={<ContactPage />} />
                    <Route path="/privacy" element={<PrivacyPolicyPage />} />
                    <Route path="/terms" element={<TermsOfServicePage />} />
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/register" element={<RegisterPage />} />
                </Routes>
            </Suspense>

            <Footer />
        </ToastProvider>
    );
}

export default App;