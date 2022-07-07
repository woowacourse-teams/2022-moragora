import OpinionItem from '.';

export default {
  title: 'Components/OpinionItem',
  component: OpinionItem,
};

const Template = (args) => {
  return <OpinionItem {...args} />;
};

export const Default = Template.bind({});
Default.args = {
  opinion: {
    id: 1,
    content: '의견입니다.',
    createdAt: 1657074309874,
    updatedAt: null,
  },
};

export const Updated = Template.bind({});
Updated.args = {
  opinion: {
    id: 1,
    content: '의견입니다.',
    createdAt: 1657074309874,
    updatedAt: 1657075309874,
  },
};
