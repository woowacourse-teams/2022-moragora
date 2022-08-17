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
                    const currentTarget =
                      e.currentTarget as HTMLInputElement & {
                        showPicker: () => void;
                      };

                    currentTarget.showPicker();
                  },
                  onChange: ({ currentTarget }) => {
                    setBeginDate(new Date(Date.parse(currentTarget.value)));
                  },
                  min: dateToFormattedString(initialDate),
                  required: true,
                  watch: true,
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
                    const currentTarget =
                      e.currentTarget as HTMLInputElement & {
                        showPicker: () => void;
                      };

                    currentTarget.showPicker();
                  },
                  onChange: ({ currentTarget }) => {
                    setEndDate(new Date(Date.parse(currentTarget.value)));
                  },
                  min: values['startDate']
                    ? (values['startDate'] as string)
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
