import MemberAddInput from '.';
import useQuerySelectItems from 'hooks/useQuerySelectItems';

export default {
  title: 'Components/MemberAddInput',
  component: MemberAddInput,
};

const Template = (args) => {
  const {
    queryResult,
    selectedItems,
    queryWithKeyword,
    selectItem,
    unselectItem,
    clearQueryResult,
  } = useQuerySelectItems('/users?keyword=', {
    wait: 150,
  });

  return (
    <MemberAddInput
      {...args}
      placeholder="닉네임 또는 이메일로 검색하세요."
      queryResult={queryResult}
      selectedItems={selectedItems}
      queryWithKeyword={queryWithKeyword}
      selectItem={selectItem}
      unselectItem={unselectItem}
      clearQueryResult={clearQueryResult}
    />
  );
};

export const Default = Template.bind({});
Default.args = {};

export const Disabled = Template.bind({});
Disabled.args = {
  disabled: true,
};
