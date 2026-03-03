// legacy hook, now backed by AuthContext

import { useAuth } from '../context/AuthContext';

export function useAuthCheck() {
    const { loggedIn, isLoading, error } = useAuth();
    return { loggedIn, isLoading, error };
}
