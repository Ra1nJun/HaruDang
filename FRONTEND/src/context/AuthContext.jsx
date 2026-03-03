import { createContext, useContext, useState, useEffect } from 'react';
import { useMe, AUTH_QUERY_KEY, login as loginApi, logout as logoutApi } from '../api/authApi';
import { useQueryClient } from '@tanstack/react-query';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const queryClient = useQueryClient();
    const { data, isLoading, error } = useMe();
    const [loggedIn, setLoggedIn] = useState(false);

    // keep context state in sync with react-query data
    useEffect(() => {
        if (data && typeof data.loggedIn === 'boolean') {
            setLoggedIn(data.loggedIn);
        }
    }, [data]);

    // helper to clear cache when manually logging out
    const clearAuth = () => {
        setLoggedIn(false);
        queryClient.removeQueries({ queryKey: [AUTH_QUERY_KEY] });
    };

    // wrappers for login/logout that update both query and context
    const login = async (email, password) => {
        const res = await loginApi(email, password);
        // optimistic
        setLoggedIn(true);
        queryClient.setQueryData([AUTH_QUERY_KEY], { loggedIn: true });
        await queryClient.invalidateQueries({ queryKey: [AUTH_QUERY_KEY] });
        return res;
    };

    const logout = async () => {
        const res = await logoutApi();
        clearAuth();
        return res;
    };

    return (
        <AuthContext.Provider value={{ loggedIn, setLoggedIn, isLoading, error, clearAuth, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const ctx = useContext(AuthContext);
    if (!ctx) throw new Error('useAuth must be used within an AuthProvider');
    return ctx;
}