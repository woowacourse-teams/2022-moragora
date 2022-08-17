import React from 'react';
import * as S from './CoffeeStackItem.styled';
import CoffeeIconSVG from 'assets/coffee.svg';
import CrownIconSVG from 'assets/crown.svg';
import { Participant } from 'types/userType_new';
import { Meeting } from 'types/meetingType_new';

type CoffeeStackItemProps = {
  name: Participant['nickname'] | Meeting['name'];
  tardyCount: Participant['tardyCount'];
  isMaster: boolean;
};

const CoffeeStackItem: React.FC<CoffeeStackItemProps> = ({
  name,
  tardyCount,
  isMaster = false,
}) => {
  return (
    <S.Layout>
      <S.NameBox>
        <span>{name}</span>
        {isMaster && <img src={CrownIconSVG} alt="crown-icon" width={20} />}
      </S.NameBox>
      <S.CoffeeStackBox>
        {Array.from({ length: tardyCount }).map((_, index) => (
          <S.CoffeeIconImage key={index} src={CoffeeIconSVG} />
        ))}
      </S.CoffeeStackBox>
    </S.Layout>
  );
};

export default CoffeeStackItem;
