import React from 'react';
import * as S from './CoffeeStackItem.styled';
import CoffeeIconSVG from '../../assets/coffee.svg';

type Meeting = {
  id: number;
  name: string;
  startDate: string;
  endDate: string;
  entranceTime: string;
  leaveTime: string;
  attendanceCount: number;
};

type CoffeeStackItemProps = {
  name: Pick<Meeting, 'name'>['name'];
};

const CoffeeStackItem: React.FC<CoffeeStackItemProps> = ({ name }) => {
  return (
    <S.Layout>
      <span>{name}</span>
      <S.Box>
        <S.CoffeeIconImage src={CoffeeIconSVG} />
        <S.CoffeeIconImage src={CoffeeIconSVG} />
        <S.CoffeeIconImage src={CoffeeIconSVG} />
      </S.Box>
    </S.Layout>
  );
};

export default CoffeeStackItem;
