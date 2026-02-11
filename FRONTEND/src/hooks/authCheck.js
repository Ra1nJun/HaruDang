import { useState, useEffect, useCallback } from 'react';
import { me } from '../api/authApi';
import { useLocation } from 'react-router-dom';

export function useAuthCheck() {
    const location = useLocation();
    const initialLoggedIn = location.state?.loggedIn || false;
    const [loggedIn, setLoggedIn] = useState(initialLoggedIn);

    const checkAuth = useCallback(async () => {
        try {
            const res = await me();
            setLoggedIn(res.data.loggedIn);
        } catch (error) {
            console.error(error);
            setLoggedIn(false);
        }
    }, []);

    useEffect(() => {
        if (initialLoggedIn) {
            setLoggedIn(true);
            return;
        }
        checkAuth();
    }, [initialLoggedIn, location.pathname, checkAuth]);

    return { loggedIn, checkAuth };
}
