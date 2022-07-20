import React, { createContext, useState } from 'react';
import { User } from 'types/userType';

type UserContextData = Pick<User, 'id' | 'email' | 'nickname'>;

export type UserContextValues = {
  user: UserContextData | null;
  setUser: React.Dispatch<React.SetStateAction<UserContextData | null>>;
};

const userContext = createContext<UserContextValues | null>(null);

const UserContextProvider: React.FC<React.PropsWithChildren> = ({
  children,
}) => {
  const [user, setUser] = useState<UserContextValues['user']>(null);

  return (
    <userContext.Provider value={{ user, setUser }}>
      {children}
    </userContext.Provider>
  );
};

export { userContext, UserContextProvider };
