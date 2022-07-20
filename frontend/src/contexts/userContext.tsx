import React, { createContext, useState } from 'react';
import { User } from 'types/userType';

type UserContextData = Pick<User, 'id' | 'email' | 'nickname' | 'accessToken'>;

export type UserContextValues = {
  user: UserContextData | null;
  login: (
    user: NonNullable<Omit<UserContextData, 'accessToken'>>,
    accessToken: NonNullable<UserContextData['accessToken']>
  ) => void;
  logout: () => void;
};

const userContext = createContext<UserContextValues | null>(null);

const UserContextProvider: React.FC<React.PropsWithChildren> = ({
  children,
}) => {
  const [user, setUser] = useState<UserContextValues['user']>(null);
  const login: UserContextValues['login'] = (user, accessToken) => {
    localStorage.setItem('accessToken', accessToken);
    setUser({ ...user, accessToken });
  };
  const logout = () => {
    localStorage.removeItem('accessToken');

    if (!user) {
      return;
    }

    setUser(null);
  };

  return (
    <userContext.Provider value={{ user, login, logout }}>
      {children}
    </userContext.Provider>
  );
};

export { userContext, UserContextProvider };
