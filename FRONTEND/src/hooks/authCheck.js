import { useMe } from '../api/authApi';

export function useAuthCheck() {
    const { data, isLoading, error } = useMe();
    
    const loggedIn = data?.loggedIn ?? false;

    return { loggedIn, isLoading, error };
}
