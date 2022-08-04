import { useContext } from 'react';
import * as S from './AdvancedConfiguration.styled';
import { CalendarContext } from 'contexts/calendarContext';
import Input from 'components/@shared/Input';
import useForm from 'hooks/useForm';
import { dateToFormattedString } from 'utils/timeUtil';

const AdvancedConfiguration = () => {
  const { initialDate, shouldApplyBeginEndDates, setBeginDate, setEndDate } =
    useContext(CalendarContext);
  const { values, errors, register } = useForm();

  if (!initialDate) {
    return <S.Layout>상위 요소에 CalendarProvider가 없습니다.</S.Layout>;
  }

  return (
    <>
      {shouldApplyBeginEndDates && (
        <S.FieldGroupBox>
          <S.FieldBox>
            <S.Label>
              시작 날짜
              <Input
                type="date"
                {...register('startDate', {
                  onClick: (e) => {
                    const target = e.target as HTMLInputElement & {
                      showPicker: () => void;
                    };

                    target.showPicker();
                  },
                  onChange: (e) => {
                    const target = e.target as HTMLInputElement;

                    setBeginDate(new Date(Date.parse(target.value)));
                  },
                  min: dateToFormattedString(initialDate),
                  required: true,
                })}
              />
            </S.Label>
          </S.FieldBox>
          <S.FieldBox>
            <S.Label>
              마감 날짜
              <Input
                type="date"
                {...register('endDate', {
                  onClick: (e) => {
                    const target = e.target as HTMLInputElement & {
                      showPicker: () => void;
                    };

                    target.showPicker();
                  },
                  onChange: (e) => {
                    const target = e.target as HTMLInputElement;

                    setEndDate(new Date(Date.parse(target.value)));
                  },
                  min: values['startDate']
                    ? values['startDate']
                    : dateToFormattedString(initialDate),
                  required: true,
                })}
                disabled={errors['startDate'] !== ''}
              />
            </S.Label>
          </S.FieldBox>
        </S.FieldGroupBox>
      )}
    </>
  );
};

export default AdvancedConfiguration;
