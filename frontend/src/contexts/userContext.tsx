import React, { createContext, useState } from 'react';
import { getLoginUserDataApi } from 'apis/userApis';
import useQuery from 'hooks/useQuery';
import { User } from 'types/userType';

type UserContextData = Omit<User, 'password'>;

export type UserContextValues = {
  user: UserContextData | null;
  getLoginUserData: () => Promise<void>;
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
    setUser({ ...user, accessToken });
  };

  const logout = () => {
    setUser(null);
  };

  const getUserDataQuery = useQuery(
    ['loginUserData'],
    getLoginUserDataApi(user?.accessToken),
    {
      enabled: !!user?.accessToken,
      refetchOnMount: false,
      onSuccess: ({ body }) => {
        if (user?.accessToken) {
          login(body, user.accessToken);
        }
      },
    }
  );

  return (
    <userContext.Provider
      value={{
        user,
        getLoginUserData: getUserDataQuery.refetch,
        login,
        logout,
      }}
    >
      {children}
    </userContext.Provider>
  );
};

export { userContext, UserContextProvider };
