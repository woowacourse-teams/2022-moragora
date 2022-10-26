import React, { createContext, useState } from 'react';
import { getLoginUserDataApi, logoutApi } from 'apis/userApis';
import useQuery from 'hooks/useQuery';
import { User } from 'types/userType';
import useMutation from 'hooks/useMutation';

type UserContextData = Omit<User, 'password'>;

export type UserContextValues = {
  user: Omit<UserContextData, 'accessToken'> | null;
  accessToken: UserContextData['accessToken'];
  initialized: boolean;
  setInitialized: React.Dispatch<React.SetStateAction<boolean>>;
  setAccessToken: React.Dispatch<
    React.SetStateAction<UserContextValues['accessToken']>
  >;
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
  const [initialized, setInitialized] = useState(false);
  const [user, setUser] = useState<UserContextValues['user']>(null);
  const [accessToken, setAccessToken] =
    useState<UserContextValues['accessToken']>(null);

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

  const login: UserContextValues['login'] = (user, accessToken) => {
    setInitialized(true);
    setUser({ ...user });
    setAccessToken(accessToken);
  };

  const logout = () => {
    setInitialized(true);
    logoutMutation.mutate({});
  };

  return (
    <userContext.Provider
      value={{
        user,
        accessToken,
        initialized,
        setInitialized,
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
