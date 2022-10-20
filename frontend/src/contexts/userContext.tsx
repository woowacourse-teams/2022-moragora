import React, { createContext, useState } from 'react';
import { getLoginUserDataApi, logoutApi } from 'apis/userApis';
import useQuery from 'hooks/useQuery';
import { User } from 'types/userType';
import useMutation from 'hooks/useMutation';

type UserContextData = Omit<User, 'password'>;

export type UserContextValues = {
  user: Omit<UserContextData, 'accessToken'> | null;
  accessToken: UserContextData['accessToken'];
  updateAccessToken: (token: UserContextData['accessToken']) => void;
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
  const [accessToken, setAccessToken] =
    useState<UserContextValues['accessToken']>(null);

  const login: UserContextValues['login'] = (user, accessToken) => {
    setUser({ ...user });
    setAccessToken(accessToken);
  };

  const logout = () => {
    logoutMutation.mutate({});
  };

  const updateAccessToken: UserContextValues['updateAccessToken'] = (token) => {
    setAccessToken(token);
  };

  const logoutMutation = useMutation(logoutApi, {
    onSuccess: () => {
      setUser(null);
      setAccessToken(null);
    },
  });

  const getUserDataQuery = useQuery(['loginUserData'], getLoginUserDataApi(), {
    enabled: !!accessToken,
    refetchOnMount: false,
    onSuccess: ({ body }) => {
      if (accessToken) {
        login(body, accessToken);
      }
    },
  });

  return (
    <userContext.Provider
      value={{
        user,
        accessToken,
        updateAccessToken,
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
