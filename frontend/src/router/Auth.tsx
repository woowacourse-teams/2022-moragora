import React, { useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';

type AuthProps = { shouldLogin: boolean };

const Auth: React.FC<AuthProps> = ({ shouldLogin }) => {
  const navigate = useNavigate();
  const accessToken = localStorage.getItem('accessToken');

  useEffect(() => {
    if (shouldLogin && !accessToken) {
      navigate('/login');
    }

    if (!shouldLogin && accessToken) {
      navigate('/');
    }
  }, [navigate, accessToken]);

  return <Outlet />;
};

export default Auth;
