import React, { useContext, useEffect, useRef } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
import { userContext, UserContextValues } from 'contexts/userContext';

type AuthProps = { shouldLogin: boolean };

const Auth: React.FC<AuthProps> = ({ shouldLogin }) => {
  const navigate = useNavigate();
  const userState = useContext(userContext) as UserContextValues;
  const initialized = useRef(true);
  const initialPath = useRef(window.location.pathname);

  useEffect(() => {
    if (shouldLogin && !userState.accessToken) {
      navigate('/login');
    }

    if (!shouldLogin && userState.accessToken) {
      if (initialized.current) {
        navigate(initialPath.current);
        initialized.current = false;
      } else {
        initialized.current = false;
        navigate('/');
      }
    }
  }, [navigate, userState]);

  return <Outlet />;
};

export default Auth;
