import DiscussionItem from '.';

export default {
  title: 'Components/DiscussionItem',
  component: DiscussionItem,
};

const Template = (args) => {
  return <DiscussionItem {...args} />;
};

export const Default = Template.bind({});
Default.args = {
  discussion: {
    id: 1,
    title: '토론 주제',
    content: '토론 내용',
    views: 138,
    createdAt: 1657074309874,
    updatedAt: null,
  },
};
