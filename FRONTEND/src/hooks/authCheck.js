import { useMe } from '../api/authApi';
import { useState, useEffect } from 'react';

export function useAuthCheck() {
    const { data, isLoading, error } = useMe();
    const [prevLogged, setPrevLogged] = useState(false);

    useEffect(() => {
        if (data && typeof data.loggedIn === 'boolean') {
            setPrevLogged(data.loggedIn);
        }
    }, [data]);

    // 로딩 중에는 이전 상태 유지
    const loggedIn = isLoading ? prevLogged : data?.loggedIn ?? false;

    return { loggedIn, isLoading, error };
}
