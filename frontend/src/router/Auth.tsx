import { useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';

const Auth = ({ isLoggedIn }: { isLoggedIn: boolean }) => {
  const navigate = useNavigate();
  const accessToken = localStorage.getItem('accessToken');

  useEffect(() => {
    if (isLoggedIn && !accessToken) {
      navigate('/login');
    }

    if (!isLoggedIn && accessToken) {
      navigate('/meeting');
    }
  }, [navigate, accessToken]);

  return <Outlet />;
};

export default Auth;
