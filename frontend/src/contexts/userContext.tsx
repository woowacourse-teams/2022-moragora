import React, { createContext, useState } from 'react';
import { getLoginUserDataApi, logoutApi } from 'apis/userApis';
import useQuery from 'hooks/useQuery';
import { User } from 'types/userType';

type UserContextData = Omit<User, 'password'>;

export type UserContextValues = {
  user: Omit<UserContextData, 'accessToken'> | null;
  accessToken: UserContextData['accessToken'];
  getLoginUserData: () => Promise<void>;
  login: (
    user: NonNullable<Omit<UserContextData, 'accessToken'>>,
    accessToken: NonNullable<UserContextData['accessToken']>
  ) => void;
  logout: () => void;
  setAccessToken: React.Dispatch<
    React.SetStateAction<UserContextData['accessToken']>
  >;
};

const userContext = createContext<UserContextValues | null>(null);

const UserContextProvider: React.FC<React.PropsWithChildren> = ({
  children,
}) => {
  const [user, setUser] = useState<UserContextValues['user']>(null);
  const [accessToken, setAccessToken] =
    useState<UserContextValues['accessToken']>(null);

  const login: UserContextValues['login'] = (user, accessToken) => {
    setUser({ ...user });
    setAccessToken(accessToken);
  };

  const logout = async () => {
    await logoutApi();
    setUser(null);
    setAccessToken(null);
  };

  const getUserDataQuery = useQuery(
    ['loginUserData'],
    getLoginUserDataApi(accessToken),
    {
      enabled: !!accessToken,
      refetchOnMount: false,
      onSuccess: ({ body }) => {
        if (accessToken) {
          login(body, accessToken);
        }
      },
    }
  );

  return (
    <userContext.Provider
      value={{
        user,
        accessToken,
        setAccessToken,
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
